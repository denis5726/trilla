package ru.trilla.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullName;
    private String code;
    @OneToMany(mappedBy = "project")
    private List<TaskFilter> taskFilters;
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<AdditionalFieldValueType> additionalFieldValueTypes;
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<UserAccess> userAccesses;
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<TaskType> taskTypes;
    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
    @CreatedDate
    private ZonedDateTime createdAt;
}
