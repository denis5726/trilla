package ru.trilla.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessagingEntityListener {
    @Setter
    @Getter
    private static MessagingEntityListener instance;
    @Setter
    private AuditEventSender auditEventSender;
    @Setter
    private ObjectMapper objectMapper;

    public MessagingEntityListener() {
        setInstance(this);
    }

    @PostPersist
    public void onCreate(Object target) {
        send(AuditEventType.CREATED, target);
    }

    @PostUpdate
    public void onUpdate(Object target) {
        send(AuditEventType.UPDATED, target);
    }

    @PostRemove
    public void onRemove(Object target) {
        send(AuditEventType.REMOVED, target);
    }

    private void send(AuditEventType type, Object target) {
        try {
            auditEventSender.sendEvent(new AuditEvent(
                type,
                    target.getClass().getSimpleName(),
                    objectMapper.writeValueAsString(target)
            ));
        } catch (JsonProcessingException e) {
            log.error("Exception when parsing object: {}", e.getMessage());
        }
    }
}
