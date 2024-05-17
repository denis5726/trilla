package ru.trilla.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.trilla.aop.LocalizedEntityName;
import ru.trilla.exception.DataValidationException;
import ru.trilla.repository.TrillaRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TrillaRepositoryImpl<T, I> extends SimpleJpaRepository<T, I> implements TrillaRepository<T, I> {
    private static final String DEFAULT_ENTITY_NAME = "Сущность";
    private static final Map<Class<?>, String> CLASS_NAME_MAP = new ConcurrentHashMap<>();

    public TrillaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public TrillaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    @Override
    public T findByIdOrThrowException(I id) {
        return findById(id).orElseThrow(() -> {
            log.info("Attempt to get {} with non-existent id: {}", getDomainClass().getSimpleName(), id);
            final var entityName = resolveEntityDescriptionName(getDomainClass());
            return new DataValidationException(String.format("%s с идентификатором %s не найден", entityName, id));
        });
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
        CLASS_NAME_MAP.put(domainClass, Objects.requireNonNullElse(localizedName.value(), DEFAULT_ENTITY_NAME));
        return CLASS_NAME_MAP.get(domainClass);
    }
}
