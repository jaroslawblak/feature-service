package com.capco.featureservice.config.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    USER(Set.of(Permission.BASIC)),
    ADMIN(Set.of(Permission.values()));

    private final Set<Permission> permissions;

     Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Collection<SimpleGrantedAuthority> getPermissions() {
        Set<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(perm -> new SimpleGrantedAuthority(perm.name()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
