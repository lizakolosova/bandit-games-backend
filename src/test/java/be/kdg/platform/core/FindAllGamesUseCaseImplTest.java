package be.kdg.platform.core;

import be.kdg.platform.domain.Game;
import be.kdg.platform.port.out.LoadGamePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAllGamesUseCaseImplTest {

    @Mock
    private LoadGamePort loadGamePort;

    @InjectMocks
    private FindAllGamesUseCaseImpl useCase;

    @Test
    void shouldReturnAllGames() {
        // arrange
        Game g1 = mock(Game.class);
        Game g2 = mock(Game.class);
        Game g3 = mock(Game.class);

        when(loadGamePort.loadAll()).thenReturn(List.of(g1, g2, g3));

        // act
        List<Game> result = useCase.findAll();

        // assert
        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of(g1, g2, g3)));
        verify(loadGamePort).loadAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoGamesExist() {
        // arrange
        when(loadGamePort.loadAll()).thenReturn(List.of());

        // act
        List<Game> result = useCase.findAll();

        // assert
        assertTrue(result.isEmpty());
        verify(loadGamePort).loadAll();
    }
}
