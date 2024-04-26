package ru.trilla.audit;

public record AuditEvent(
        AuditEventType type,
        String objectType,
        String body
) {
}
