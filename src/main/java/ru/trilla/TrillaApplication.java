package ru.trilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.trilla.repository.impl.TrillaRepositoryImpl;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableJpaRepositories(repositoryBaseClass = TrillaRepositoryImpl.class)
public class TrillaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrillaApplication.class, args);
    }
}
