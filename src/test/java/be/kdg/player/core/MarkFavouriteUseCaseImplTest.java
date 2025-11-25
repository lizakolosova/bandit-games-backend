package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.MarkFavouriteCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkFavouriteUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @Mock
    private UpdatePlayerPort updatePlayerPort;

    @InjectMocks
    private MarkFavouriteUseCaseImpl useCase;

    @Test
    void shouldMarkFavouriteWhenFavouriteIsTrue() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        MarkFavouriteCommand command = new MarkFavouriteCommand(playerId, gameId, true);

        Player player = mock(Player.class);

        when(loadPlayerPort.loadById(any(PlayerId.class))).thenReturn(Optional.of(player));

        // act
        useCase.markFavourite(command);

        // assert
        verify(player).markGameAsFavourite(gameId);
        verify(updatePlayerPort).update(player);
    }

    @Test
    void shouldUnmarkFavouriteWhenFavouriteIsFalse() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        MarkFavouriteCommand command = new MarkFavouriteCommand(playerId, gameId, false);

        Player player = mock(Player.class);

        when(loadPlayerPort.loadById(any(PlayerId.class))).thenReturn(Optional.of(player));

        // act
        useCase.markFavourite(command);

        // assert
        verify(player).unmarkGameAsFavourite(gameId);
        verify(updatePlayerPort).update(player);
    }

    @Test
    void shouldThrowExceptionWhenPlayerNotFound() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        MarkFavouriteCommand command = new MarkFavouriteCommand(playerId, gameId, true);

        when(loadPlayerPort.loadById(any(PlayerId.class))).thenReturn(Optional.empty());

        // act + assert
        assertThrows(NoSuchElementException.class, () -> useCase.markFavourite(command));

        verify(updatePlayerPort, never()).update(any());
    }

}