package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.UserAccess;

import java.util.List;
import java.util.UUID;

public interface UserAccessRepository extends JpaRepository<UserAccess, UserAccess.Id> {

    List<UserAccess> findByIdUserId(UUID userId);
}
