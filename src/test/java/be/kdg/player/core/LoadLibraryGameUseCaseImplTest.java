package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.player.adapter.in.response.LibraryGameDetailsDto;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.LoadLibraryGameCommand;
import be.kdg.player.port.out.LoadGameProjectionPort;
import be.kdg.player.port.out.LoadPlayerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadLibraryGameUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @Mock
    private LoadGameProjectionPort loadGameProjectionPort;

    @InjectMocks
    private LoadLibraryGameUseCaseImpl useCase;

    @Test
    void shouldReturnMappedDtoWhenGameIsLoaded() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        LoadLibraryGameCommand command = new LoadLibraryGameCommand(playerId, gameId);

        Player player = mock(Player.class);
        GameLibrary libraryEntry = mock(GameLibrary.class);
        GameProjection projection = mock(GameProjection.class);

        when(loadPlayerPort.loadById(any())).thenReturn(Optional.of(player));
        when(player.findGameInLibrary(gameId)).thenReturn(libraryEntry);
        when(player.getAchievements()).thenReturn(Collections.emptySet());

        String name = "Test Game";
        String pictureUrl = "http://example.com/pic.png";
        String category = "Adventure";
        int achievementCount = 10;
        int averageMinutes = 120;
        String developedBy = "Test Studio";

        when(projection.getGameId()).thenReturn(gameId);
        when(projection.getName()).thenReturn(name);
        when(projection.getPictureUrl()).thenReturn(pictureUrl);
        when(projection.getCategory()).thenReturn(category);
        when(projection.getAchievementCount()).thenReturn(achievementCount);
        when(projection.getAverageMinutes()).thenReturn(averageMinutes);
        when(projection.getDevelopedBy()).thenReturn(developedBy);

        when(loadGameProjectionPort.loadProjection(gameId)).thenReturn(projection);

        Duration totalPlaytime = Duration.ofMinutes(90);
        when(libraryEntry.getAddedAt()).thenReturn(null);
        when(libraryEntry.getLastPlayedAt()).thenReturn(null);
        when(libraryEntry.getTotalPlaytime()).thenReturn(totalPlaytime);
        when(libraryEntry.isFavourite()).thenReturn(true);

        // act
        LibraryGameDetailsDto result = useCase.loadGame(command);

        // assert
        assertNotNull(result);
        assertEquals(gameId, result.gameId());
        assertEquals(name, result.name());
        assertEquals(pictureUrl, result.pictureUrl());
        assertEquals(category, result.category());
        assertEquals(achievementCount, result.achievementCount());
        assertEquals(averageMinutes, result.averageMinutes());
        assertEquals(developedBy, result.developedBy());

        assertEquals(totalPlaytime.toMinutes(), result.totalMinutes());
        assertTrue(result.favourite());

        assertEquals(0, result.unlockedCount());
        assertTrue(result.unlockedAchievements().isEmpty());

        verify(loadPlayerPort).loadById(any());
        verify(player).findGameInLibrary(gameId);
        verify(loadGameProjectionPort).loadProjection(gameId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPlayerNotFound() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        LoadLibraryGameCommand command = new LoadLibraryGameCommand(playerId, gameId);

        when(loadPlayerPort.loadById(any())).thenReturn(Optional.empty());

        // act + assert
        assertThrows(NotFoundException.class, () -> useCase.loadGame(command));

        // Projection should never be called when player is missing
        verify(loadGameProjectionPort, never()).loadProjection(any());
    }
}