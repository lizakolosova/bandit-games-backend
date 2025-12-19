package be.kdg.player.core;

import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.RegisterPlayerCommand;
import be.kdg.player.port.out.UpdatePlayerPort;
import be.kdg.security.service.KeycloakUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPlayerUseCaseImplTest {

    @Mock
    private UpdatePlayerPort updatePlayerPort;

    @Mock
    private KeycloakUserService keycloakUserService;

    private RegisterPlayerUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterPlayerUseCaseImpl(updatePlayerPort, keycloakUserService);
    }

    @Test
    void shouldRegisterPlayer() {
        // Arrange
        RegisterPlayerCommand command =
                new RegisterPlayerCommand("geetika", "geetika@example.com", "paasword", "url");

        // Act
        Player result = useCase.register(command);

        // Assert: update is called with a new Player with expected data
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(updatePlayerPort).update(captor.capture());

        Player saved = captor.getValue();
        assertThat(saved.getPlayerId()).isEqualTo(result.getPlayerId());
        assertThat(saved.getUsername()).isEqualTo("geetika");
        assertThat(saved.getEmail()).isEqualTo("geetika@example.com");
        assertThat(saved.getPictureUrl()).isEqualTo("url");

        // The returned Player mirrors the updated one
        assertThat(result.getPlayerId()).isEqualTo(saved.getPlayerId());
        assertThat(result.getUsername()).isEqualTo(saved.getUsername());
        assertThat(result.getEmail()).isEqualTo(saved.getEmail());
    }
}