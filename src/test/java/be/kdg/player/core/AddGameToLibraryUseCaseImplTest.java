package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.AddGameToLibraryCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import be.kdg.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddGameToLibraryUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @Mock
    private UpdatePlayerPort updatePlayerPort;

    private AddGameToLibraryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddGameToLibraryUseCaseImpl(loadPlayerPort, updatePlayerPort);
    }

    @Test
    void shouldAddGameToLibrary() {
        // Arrange
        UUID playerUuid = UUID.randomUUID();
        UUID gameUuid = UUID.randomUUID();

        PlayerId playerId = PlayerId.of(playerUuid);
        AddGameToLibraryCommand command =
                new AddGameToLibraryCommand(playerUuid, gameUuid);

        Player player = mock(Player.class);
        GameLibrary library = mock(GameLibrary.class);

        when(loadPlayerPort.loadById(playerId)).thenReturn(Optional.of(player));
        when(player.addGameToLibrary(gameUuid)).thenReturn(library);

        // Act
        GameLibrary result = useCase.addGameToLibrary(command);

        // Assert
        verify(loadPlayerPort).loadById(playerId);
        verify(player).addGameToLibrary(gameUuid);
        verify(updatePlayerPort).update(player);
        assertSame(library, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPlayerDoesNotExist() {
        // Arrange
        UUID playerUuid = UUID.randomUUID();
        UUID gameUuid = UUID.randomUUID();

        PlayerId playerId = PlayerId.of(playerUuid);
        AddGameToLibraryCommand command =
                new AddGameToLibraryCommand(playerUuid, gameUuid);

        when(loadPlayerPort.loadById(playerId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> useCase.addGameToLibrary(command));

        verify(updatePlayerPort, never()).update(any());
    }

}