package be.kdg.player.adapter.out;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class KeycloakUserSyncService {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserSyncService.class);

    private final PlayerJpaRepository playerRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;


    public KeycloakUserSyncService(PlayerJpaRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostConstruct
    public void syncUsers() {
        try {
            logger.info("=== Starting Keycloak user sync ===");

            // Get admin token
            String token = getAdminToken();
            logger.info("Admin token acquired");

            // Fetch users from Keycloak
            String url = keycloakUrl + "/admin/realms/" + realm + "/users";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            List<Map<String, Object>> users = response.getBody();
            logger.info("Found {} users in Keycloak", users != null ? users.size() : 0);

            if (users != null) {
                for (Map<String, Object> user : users) {
                    String id = (String) user.get("id");
                    String username = (String) user.get("username");
                    String email = (String) user.get("email");

                    UUID uuid = UUID.fromString(id);

                    // Check if player already exists
                    if (!playerRepository.existsById(uuid)) {
                        PlayerJpaEntity player = new PlayerJpaEntity(
                                uuid,
                                username,
                                email != null ? email : username + "@example.com",
                                null, // pictureUrl
                                LocalDateTime.now()
                        );
                        playerRepository.save(player);
                        logger.info("Synced player: {} ({})", username, uuid);
                    } else {
                        logger.debug("Player already exists: {}", username);
                    }
                }
            }

            logger.info("=== User sync completed ===");

        } catch (Exception e) {
            logger.error("Failed to sync users from Keycloak", e);
        }
    }

    private String getAdminToken() {
        try {
            String tokenUrl = keycloakUrl + "/realms/master/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", "admin-cli");
            body.add("username", "admin");
            body.add("password", "admin");
            body.add("grant_type", "password");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            }

            throw new RuntimeException("Failed to get access token from Keycloak");

        } catch (Exception e) {
            logger.error("Failed to get admin token", e);
            throw new RuntimeException("Cannot authenticate with Keycloak admin", e);
        }
    }
}