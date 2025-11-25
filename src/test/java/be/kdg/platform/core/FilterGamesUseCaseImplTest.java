package be.kdg.platform.core;

import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.FilterGamesCommand;
import be.kdg.platform.port.out.LoadGamePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterGamesUseCaseImplTest {

    @Mock
    private LoadGamePort loadGamePort;

    @InjectMocks
    private FilterGamesUseCaseImpl useCase;

    @Test
    void shouldReturnAllGamesWhenCategoryIsEmpty() {
        // arrange
        Game g1 = mock(Game.class);
        Game g2 = mock(Game.class);
        Game g3 = mock(Game.class);

        when(loadGamePort.loadAll()).thenReturn(List.of(g1, g2, g3));

        FilterGamesCommand command = new FilterGamesCommand(Optional.empty());

        // act
        List<Game> result = useCase.filter(command);

        // assert
        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of(g1, g2, g3)));
    }

    @Test
    void shouldReturnGamesMatchingCategory() {
        // arrange
        Game puzzle1 = mock(Game.class);
        Game puzzle2 = mock(Game.class);
        Game action1 = mock(Game.class);

        when(puzzle1.getCategory()).thenReturn("Puzzle");
        when(puzzle2.getCategory()).thenReturn("Puzzle");
        when(action1.getCategory()).thenReturn("Action");

        when(loadGamePort.loadAll()).thenReturn(List.of(puzzle1, puzzle2, action1));

        FilterGamesCommand command = new FilterGamesCommand(Optional.of("Puzzle"));

        // act
        List<Game> result = useCase.filter(command);

        // assert
        assertEquals(2, result.size());
        assertTrue(result.contains(puzzle1));
        assertTrue(result.contains(puzzle2));
        assertFalse(result.contains(action1));
    }

    @Test
    void shouldReturnEmptyListWhenNoGamesMatchCategory() {
        // arrange
        Game g1 = mock(Game.class);
        Game g2 = mock(Game.class);

        when(g1.getCategory()).thenReturn("Puzzle");
        when(g2.getCategory()).thenReturn("Adventure");

        when(loadGamePort.loadAll()).thenReturn(List.of(g1, g2));

        FilterGamesCommand command = new FilterGamesCommand(Optional.of("Horror"));

        // act
        List<Game> result = useCase.filter(command);

        // assert
        assertTrue(result.isEmpty());
    }
}
