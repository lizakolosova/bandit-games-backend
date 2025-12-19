package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.PlayerEventPublisher;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.RemoveFriendCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveFriendUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @Mock
    private UpdatePlayerPort updatePlayerPort;

    @Mock
    private PlayerEventPublisher eventPublisher;

    @InjectMocks
    private RemoveFriendUseCaseImpl sut;

    @Test
    void shouldRemoveFriendAndSavePlayer() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();

        Player player = spy(new Player(PlayerId.of(playerId), "john", "john@test.com", "pic.png", LocalDateTime.now()));
        player.addFriend(friendId);

        when(loadPlayerPort.loadById(PlayerId.of(playerId)))
                .thenReturn(Optional.of(player));

        RemoveFriendCommand command = new RemoveFriendCommand(playerId, friendId);

        // act
        sut.removeFriend(command);

        // assert
        verify(player).removeFriend(friendId);
        verify(updatePlayerPort).update(player);
        verify(eventPublisher).publishEvents(anyList());
    }

    @Test
    void shouldThrowWhenPlayerNotFound() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();

        when(loadPlayerPort.loadById(PlayerId.of(playerId)))
                .thenReturn(Optional.empty());

        RemoveFriendCommand command = new RemoveFriendCommand(playerId, friendId);

        // act + assert
        assertThrows(NotFoundException.class, () -> sut.removeFriend(command));
        verify(updatePlayerPort, never()).update(any());
    }
}
