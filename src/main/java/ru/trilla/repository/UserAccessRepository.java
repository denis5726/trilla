package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.Project;
import ru.trilla.entity.Role;
import ru.trilla.entity.UserAccess;

import java.util.List;
import java.util.UUID;

public interface UserAccessRepository extends JpaRepository<UserAccess, UserAccess.Id> {

    List<UserAccess> findByIdUserId(UUID userId);

    boolean existsByIdUserIdAndIdProjectId(UUID userId, UUID projectId);

    List<UserAccess> findByIdProjectId(UUID projectId);

    void deleteByIdUserIdAndIdProjectId(UUID userId, UUID projectId);

    boolean existsByIdUserIdAndIdProjectAndRole(UUID userId, Project project, Role role);
}
