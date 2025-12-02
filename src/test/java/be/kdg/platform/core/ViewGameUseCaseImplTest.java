package be.kdg.platform.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.command.ViewGameCommand;
import be.kdg.platform.port.out.LoadGamePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewGameUseCaseImplTest {

    @Mock
    private LoadGamePort loadGamePort;

    @InjectMocks
    private ViewGameUseCaseImpl useCase;

    @Test
    void shouldReturnGameWhenGameExists() throws GameNotFoundException {
        // arrange
        UUID gameId = UUID.randomUUID();
        ViewGameCommand command = new ViewGameCommand(gameId);

        Game game = mock(Game.class);

        when(loadGamePort.loadById(any(GameId.class))).thenReturn(Optional.of(game));

        // act
        Game result = useCase.viewGame(command);

        // assert
        assertNotNull(result);
        assertSame(game, result);
        verify(loadGamePort).loadById(any(GameId.class));
    }

    @Test
    void shouldThrowGameNotFoundExceptionWhenGameDoesNotExist() {
        // arrange
        UUID gameId = UUID.randomUUID();
        ViewGameCommand command = new ViewGameCommand(gameId);

        when(loadGamePort.loadById(any(GameId.class))).thenReturn(Optional.empty());

        // act + assert
        assertThrows(GameNotFoundException.class, () -> useCase.viewGame(command));

        verify(loadGamePort).loadById(any(GameId.class));
    }
}