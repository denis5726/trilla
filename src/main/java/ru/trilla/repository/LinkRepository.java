package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.Link;
import ru.trilla.entity.LinkType;

import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {

    void deleteByIdLinkType(LinkType linkType);
}
