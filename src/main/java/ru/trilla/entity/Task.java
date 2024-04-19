package ru.trilla.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;
    private String summary;
    private String description;
    @ManyToOne
    @JoinColumn
    private User creator;
    @ManyToOne
    @JoinColumn
    private User assignee;
    @ManyToOne
    @JoinColumn
    private Project project;
    @ManyToOne
    @JoinColumn
    private TaskType taskType;
    @ManyToOne
    @JoinColumn
    private TaskStatus taskStatus;
    @CreatedDate
    private ZonedDateTime createdAt;
    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
