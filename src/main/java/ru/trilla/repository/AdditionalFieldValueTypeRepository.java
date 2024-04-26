package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.AdditionalFieldValueType;

import java.util.UUID;

public interface AdditionalFieldValueTypeRepository extends JpaRepository<AdditionalFieldValueType, UUID> {
}
