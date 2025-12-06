package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.LoadGameLibraryCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadGameLibraryUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @InjectMocks
    private LoadGameLibraryUseCaseImpl useCase;

    @Test
    void shouldReturnGameLibrariesWhenPlayerExists() {
        // arrange
        UUID playerId = UUID.randomUUID();
        LoadGameLibraryCommand command = new LoadGameLibraryCommand(playerId);

        Player player = mock(Player.class);
        GameLibrary game1 = mock(GameLibrary.class);
        GameLibrary game2 = mock(GameLibrary.class);

        // Player is found
        when(loadPlayerPort.loadById(any())).thenReturn(Optional.of(player));
        when(player.getGameLibraries()).thenReturn(Set.of(game1, game2));

        // act
        List<GameLibrary> result = useCase.loadLibrary(command);

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));

        verify(loadPlayerPort).loadById(any());
        verify(player).getGameLibraries();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPlayerNotFound() {
        // arrange
        UUID playerId = UUID.randomUUID();
        LoadGameLibraryCommand command = new LoadGameLibraryCommand(playerId);

        when(loadPlayerPort.loadById(any())).thenReturn(Optional.empty());

        // act + assert
        assertThrows(NotFoundException.class, () -> useCase.loadLibrary(command));

        verify(loadPlayerPort).loadById(any());
    }
}