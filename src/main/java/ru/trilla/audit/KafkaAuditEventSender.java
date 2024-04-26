package ru.trilla.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAuditEventSender implements AuditEventSender {
    private final ObjectMapper objectMapper;

    // TODO Реализовать отправку событий
    @Override
    public void sendEvent(AuditEvent event) {
        try {
            log.info("Posting audit event: {}", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("Exception when parsing event: {}", e.getMessage());
        }
    }
}
