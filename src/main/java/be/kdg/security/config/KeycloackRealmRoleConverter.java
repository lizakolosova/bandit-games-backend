package be.kdg.security.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloackRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Check for CLIENT roles (resource_access.kdg-frontend.roles)
        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
        if (resourceAccess != null) {
            Map<String, Object> kdgFrontend = (Map<String, Object>) resourceAccess.get("kdg-frontend");
            if (kdgFrontend != null) {
                Object rolesObj = kdgFrontend.get("roles");
                if (rolesObj != null) {
                    return ((List<String>) rolesObj).stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                }
            }
        }

        // Fallback to realm roles (realm_access.roles)
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj != null) {
                return ((List<String>) rolesObj).stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            }
        }

        // No roles found (public endpoints without token)
        return Collections.emptyList();
    }
}