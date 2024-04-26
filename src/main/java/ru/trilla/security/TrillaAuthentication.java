package ru.trilla.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.trilla.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record TrillaAuthentication(
        UUID id,
        List<ProjectRole> roles
) implements Authentication {

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public Object getCredentials() {
        return null;
    }

    @Override
    @JsonIgnore
    public Object getDetails() {
        return null;
    }

    @Override
    @JsonIgnore
    public Object getPrincipal() {
        return id;
    }

    @Override
    @JsonIgnore
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            throw new IllegalArgumentException("TrillaAuthentication cannot be unauthenticated!");
        }
    }

    @Override
    @JsonIgnore
    public String getName() {
        return id.toString();
    }

    public record ProjectRole(
            UUID projectId,
            Role role
    ) implements GrantedAuthority {

        @Override
        @JsonIgnore
        public String getAuthority() {
            return projectId + ":" + role;
        }
    }
}
