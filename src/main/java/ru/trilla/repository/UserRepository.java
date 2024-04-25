package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
