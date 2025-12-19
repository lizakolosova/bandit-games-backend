package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.RegisterPlayerCommand;
import be.kdg.player.port.in.RegisterPlayerUseCase;
import be.kdg.player.port.out.UpdatePlayerPort;
import be.kdg.security.service.KeycloakUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegisterPlayerUseCaseImpl implements RegisterPlayerUseCase {

    private final UpdatePlayerPort updatePlayerPort;
    private final KeycloakUserService keycloakUserService;
    private final Logger log =  LoggerFactory.getLogger(RegisterPlayerUseCaseImpl.class);

    public RegisterPlayerUseCaseImpl(UpdatePlayerPort updatePlayerPort, KeycloakUserService keycloakUserService) {
        this.updatePlayerPort = updatePlayerPort;
        this.keycloakUserService = keycloakUserService;
    }

    @Override
    @Transactional
    public Player register(RegisterPlayerCommand command) {
        try {
            log.info("Starting registration for user: {}", command.username());

            UUID keycloakUserId = keycloakUserService.createKeycloakUser(
                    command.username(),
                    command.email(),
                    command.password()
            );

            log.info("Created Keycloak user ID: {}", keycloakUserId);

            Player player = new Player(
                    PlayerId.of(keycloakUserId),
                    command.username(),
                    command.email(),
                    command.pictureUrl(),
                    java.time.LocalDateTime.now()
            );

            updatePlayerPort.update(player);
            log.info("Saved player to database");

            return player;
        } catch (Exception e) {
            log.error("Registration failed for user: {}", command.username(), e);
            throw e;
        }
    }
}
