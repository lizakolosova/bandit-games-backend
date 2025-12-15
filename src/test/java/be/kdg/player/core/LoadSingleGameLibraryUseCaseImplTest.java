package be.kdg.player.core;

import static org.junit.jupiter.api.Assertions.*;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.LoadSingleGameLibraryCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class LoadSingleGameLibraryUseCaseImplTest {

    private LoadPlayerPort loadPlayerPort;
    private LoadSingleGameLibraryUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        loadPlayerPort = mock(LoadPlayerPort.class);
        useCase = new LoadSingleGameLibraryUseCaseImpl(loadPlayerPort);
    }

    @Test
    void shouldReturnGameLibrary() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        GameLibrary libraryEntry = new GameLibrary(gameId);
        Player mockPlayer = mock(Player.class);

        when(loadPlayerPort.loadById(PlayerId.of(playerId)))
                .thenReturn(Optional.of(mockPlayer));

        when(mockPlayer.findGameInLibrary(gameId))
                .thenReturn(libraryEntry);

        LoadSingleGameLibraryCommand command =
                new LoadSingleGameLibraryCommand(playerId, gameId);

        // Act
        GameLibrary result = useCase.loadGame(command);

        // Assert
        assertNotNull(result);
        assertEquals(gameId, result.getGameId());
        verify(loadPlayerPort).loadById(PlayerId.of(playerId));
        verify(mockPlayer).findGameInLibrary(gameId);
    }


    @Test
    void WhenLibraryNotFoundThrowsException() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        when(loadPlayerPort.loadById(PlayerId.of(playerId)))
                .thenReturn(Optional.empty());

        LoadSingleGameLibraryCommand command =
                new LoadSingleGameLibraryCommand(playerId, gameId);

        // Act + Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.loadGame(command));

        assertTrue(ex.getMessage().contains(playerId.toString()));
        verify(loadPlayerPort).loadById(PlayerId.of(playerId));
        verifyNoMoreInteractions(loadPlayerPort);
    }
}
