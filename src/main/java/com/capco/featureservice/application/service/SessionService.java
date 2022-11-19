package com.capco.featureservice.application.service;

import com.capco.featureservice.config.security.Permission;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    public Set<Permission> getPermissions() {
        return this.getAuthentication().getAuthorities()
                .stream()
                .filter(perm -> !perm.getAuthority().startsWith("ROLE_"))
                .map(grantedAuthority -> Permission.fromName(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet());
    }
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();

    }
}
