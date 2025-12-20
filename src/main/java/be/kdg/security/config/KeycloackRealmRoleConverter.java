package be.kdg.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(KeycloackRealmRoleConverter.class);

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        logger.info("=== Converting JWT to authorities ===");
        logger.debug("JWT claims: {}", jwt.getClaims());

        try {
            // Check for CLIENT roles (resource_access.kdg-frontend.roles)
            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
            logger.info("resource_access found: {}", resourceAccess != null);

            if (resourceAccess != null) {
                Map<String, Object> kdgFrontend = (Map<String, Object>) resourceAccess.get("kdg-frontend");
                logger.info("kdg-frontend found: {}", kdgFrontend != null);

                if (kdgFrontend != null) {
                    Object rolesObj = kdgFrontend.get("roles");
                    logger.info("Client roles found: {}", rolesObj);

                    if (rolesObj instanceof List) {
                        List<String> roles = (List<String>) rolesObj;
                        Collection<GrantedAuthority> authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList());
                        logger.info(" Converted client roles to authorities: {}", authorities);
                        return authorities;
                    }
                }
            }

            // Fallback to realm roles (realm_access.roles)
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            logger.info("realm_access found: {}", realmAccess != null);

            if (realmAccess != null) {
                Object rolesObj = realmAccess.get("roles");
                logger.info("Realm roles found: {}", rolesObj);

                if (rolesObj instanceof List) {
                    List<String> roles = (List<String>) rolesObj;
                    Collection<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                    logger.info(" Converted realm roles to authorities: {}", authorities);
                    return authorities;
                }
            }

            logger.warn(" No roles found in JWT - returning empty authorities");
            return Collections.emptyList();

        } catch (Exception e) {
            logger.error(" Error converting JWT to authorities", e);
            return Collections.emptyList();
        }
    }
}