package ru.trilla.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.trilla.audit.KafkaAuditEventSender;
import ru.trilla.audit.MessagingEntityListener;

import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
public class JpaConfig {

    @Bean
    DateTimeProvider zonedDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    MessagingEntityListener messagingEntityListener(
            KafkaAuditEventSender kafkaAuditEventSender,
            ObjectMapper objectMapper
    ) {
        final var listener = MessagingEntityListener.getInstance();
        listener.setAuditEventSender(kafkaAuditEventSender);
        listener.setObjectMapper(objectMapper);
        return listener;
    }
}
