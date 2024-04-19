package ru.trilla.security;

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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
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
    public String getName() {
        return id.toString();
    }

    public record ProjectRole(
            UUID projectId,
            Role role
    ) implements GrantedAuthority {

        @Override
        public String getAuthority() {
            return projectId + ":" + role;
        }
    }
}
