package ru.trilla.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private static final String BODY_KEY = "body";
    private static final Long EXPIRATION_IN_SECONDS = 86400L;
    @Value("${trilla.jwt.secret}")
    private final String secret;
    private final ObjectMapper objectMapper;
    private String encodedSecret;

    @PostConstruct
    void init() {
        encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isValidToken(String token) {
        try {
            final var claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (RuntimeException e) {
            log.error("Exception during parsing token: {}", e.getMessage());
            return false;
        }
    }

    public String getToken(TrillaAuthentication authentication) {
        final var claims = Jwts.claims();
        try {
            claims.put(BODY_KEY, objectMapper.writeValueAsString(authentication));
        } catch (JsonProcessingException e) {
            log.error("Exception during writing authentication in token: {}", e.getMessage());
            throw new IllegalArgumentException("Exception when writing token body", e);
        }
        final var now = new Date();
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_IN_SECONDS * 1000L))
                .signWith(SignatureAlgorithm.HS256, encodedSecret)
                .compact();
    }

    public Optional<TrillaAuthentication> getAuthentication(String token) {
        if (!isValidToken(token)) {
            return Optional.empty();
        }
        try {
            final var claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token).getBody();
            return Optional.ofNullable(
                    objectMapper.readValue(claims.get(BODY_KEY, String.class), TrillaAuthentication.class)
            );
        } catch (Exception e) {
            log.error("Exception during parsing token: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
