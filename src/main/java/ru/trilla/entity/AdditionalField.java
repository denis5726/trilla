package ru.trilla.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.trilla.aop.LocalizedEntityName;
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
@LocalizedEntityName("Дополнительное поле")
public class AdditionalField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String value;
    @ManyToOne
    @JoinColumn
    private AdditionalFieldType additionalFieldType;
    @ManyToOne
    @JoinColumn
    private Task task;
    @CreatedDate
    private ZonedDateTime createdAt;
    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
