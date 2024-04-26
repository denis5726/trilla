package ru.trilla.audit;

public interface AuditEventSender {

    void sendEvent(AuditEvent event);
}
