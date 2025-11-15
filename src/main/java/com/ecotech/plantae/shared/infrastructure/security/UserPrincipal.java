package com.ecotech.plantae.shared.infrastructure.security;

import com.ecotech.plantae.iam.domain.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(String id,
                            String email,
                            String password,
                            String role,
                            String language,
                            String accountType,
                            boolean active) implements UserDetails {

    public static UserPrincipal from(User user) {
        return new UserPrincipal(
                user.getId().value(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole().name(),
                user.getLanguage().name().toLowerCase(),
                user.getAccountType().name(),
                user.isActive()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
