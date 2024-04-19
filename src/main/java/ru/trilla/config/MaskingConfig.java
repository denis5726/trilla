package ru.trilla.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.trilla.masking.MaskingFilter;
import ru.trilla.masking.ObjectMasker;

import java.util.List;

@Configuration
public class MaskingConfig {

    @Bean
    MaskingFilter maskingFilter(List<ObjectMasker> maskers) {
        final var instance = MaskingFilter.getInstance();
        instance.setMaskers(maskers);
        return instance;
    }
}
