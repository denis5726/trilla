package ru.trilla.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.trilla.audit.MessagingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners({AuditingEntityListener.class, MessagingEntityListener.class})
public class Link {
    @EmbeddedId
    private Id id;
    @CreatedDate
    private ZonedDateTime createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class Id {
        @ManyToOne
        @JoinColumn
        private Task from;
        @ManyToOne
        @JoinColumn
        private Task in;
        @ManyToOne
        @JoinColumn
        private LinkType linkType;
    }
}
