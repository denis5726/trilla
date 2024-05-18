package ru.trilla.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = OpenApiDefinition.MAIN_SECURITY_SCHEME, paramName = "Authorization",
        in = SecuritySchemeIn.HEADER, bearerFormat = "JWT")
@OpenAPIDefinition(
    info = @Info(
            title = "Trilla API", version = "1.0.0",
            contact = @Contact(
                    name = "Gordeev Denis",
                    email = "dengordeev1@gmail.com"
            )
    )
)
@SuppressWarnings("squid:S1118")
public class OpenApiDefinition {
    public static final String MAIN_SECURITY_SCHEME = "Main-security";
}
