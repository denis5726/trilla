package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.UserAccess;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccessRepository extends JpaRepository<UserAccess, UserAccess.Id> {

    List<UserAccess> findByIdUserId(UUID userId);

    boolean existsByIdUserIdAndIdProjectId(UUID userId, UUID projectId);

    Optional<UserAccess> findByIdUserIdAndIdProjectId(UUID userId, UUID projectId);
}
