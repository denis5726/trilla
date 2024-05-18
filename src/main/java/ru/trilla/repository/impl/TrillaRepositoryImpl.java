package ru.trilla.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;
import ru.trilla.aop.LocalizedEntityName;
import ru.trilla.aop.UniqueField;
import ru.trilla.exception.DataValidationException;
import ru.trilla.exception.ResourceAlreadyExistsException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class TrillaRepositoryImpl<T, I> extends SimpleJpaRepository<T, I> {
    private static final String DEFAULT_ENTITY_NAME = "Сущность";
    private static final String GETTER_PREFIX = "get";
    private static final Map<Class<?>, String> CLASS_NAME_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Set<String>> UNIQUE_FIELDS_MAP = new ConcurrentHashMap<>();
    private final EntityManager entityManager;

    public TrillaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public TrillaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @NonNull
    public Optional<T> findById(@NonNull I id) {
        final var result = super.findById(id).orElseThrow(() -> {
            log.info("Attempt to get {} with non-existent id: {}", getDomainClass().getSimpleName(), id);
            final var entityName = resolveEntityDescriptionName(getDomainClass());
            return new DataValidationException(String.format("%s с идентификатором %s не найден", entityName, id));
        });
        return Optional.of(result);
    }

    @Override
    @NonNull
    public <S extends T> S save(@NonNull S entity) {
        final var domainClass = getDomainClass();
        final var uniqueFields = resolveUniqueFields(domainClass);
        uniqueFields.forEach(field -> checkIdentityByField(entity, domainClass, field));
        return super.save(entity);
    }

    private String resolveEntityDescriptionName(Class<T> domainClass) {
        if (CLASS_NAME_MAP.containsKey(domainClass)) {
            return CLASS_NAME_MAP.get(domainClass);
        }
        final var localizedName = domainClass.getAnnotation(LocalizedEntityName.class);
        if (Objects.isNull(localizedName)) {
            CLASS_NAME_MAP.put(domainClass, DEFAULT_ENTITY_NAME);
            return DEFAULT_ENTITY_NAME;
        }
        final var result = Objects.requireNonNullElse(localizedName.value(), DEFAULT_ENTITY_NAME);
        CLASS_NAME_MAP.put(domainClass, result);
        return result;
    }

    private Set<String> resolveUniqueFields(Class<T> domainClass) {
        if (UNIQUE_FIELDS_MAP.containsKey(domainClass)) {
            return UNIQUE_FIELDS_MAP.get(domainClass);
        }
        final var result = Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> Objects.nonNull(field.getAnnotation(UniqueField.class)))
                .map(Field::getName)
                .collect(Collectors.toSet());
        UNIQUE_FIELDS_MAP.put(domainClass, result);
        return result;
    }

    private <S extends T> void checkIdentityByField(S entity, Class<T> domainClass, String field) {
        try {
            final var fieldValue = getFieldValue(entity, domainClass, field);
            final Long duplicatesCount = executeUniqueQuery(domainClass, field, fieldValue);
            if (duplicatesCount > 0) {
                throw new ResourceAlreadyExistsException(String.format(
                        "%s с %s=%s уже существует",
                        resolveEntityDescriptionName(domainClass),
                        field,
                        fieldValue
                ));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(
                    "Auto unique checking failed for class={}, field={}: {}",
                    domainClass.getSimpleName(),
                    field,
                    e.getMessage()
            );
        }
    }

    private <S extends T> Object getFieldValue(S entity, Class<T> domainClass, String field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return domainClass.getDeclaredMethod(GETTER_PREFIX + StringUtils.capitalize(field)).invoke(entity);
    }

    private Long executeUniqueQuery(Class<T> domainClass, String field, Object fieldValue) {
        final var cb = entityManager.getCriteriaBuilder();
        final var query = cb.createQuery(Long.class);
        final var root = query.from(domainClass);
        query.select(cb.count(root.get(field)).as(Long.class));
        query.where(cb.equal(root.get(field), fieldValue));
        return entityManager.createQuery(query).getSingleResult();
    }
}
