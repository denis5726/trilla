package ru.trilla.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.trilla.aop.LocalizedEntityName;
import ru.trilla.aop.UniqueField;
import ru.trilla.audit.MessagingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners({AuditingEntityListener.class, MessagingEntityListener.class})
@LocalizedEntityName("Проект")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullName;
    @UniqueField
    private String code;
    @CreatedDate
    private ZonedDateTime createdAt;
}
