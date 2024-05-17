package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TrillaRepository<T, I> extends JpaRepository<T, I> {

    T findByIdOrThrowException(I id);
}
