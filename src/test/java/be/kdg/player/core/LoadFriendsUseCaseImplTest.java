package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.valueobj.Friend;
import be.kdg.player.port.out.LoadPlayerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadFriendsUseCaseImplTest {

    @Mock
    private LoadPlayerPort loadPlayerPort;

    private LoadFriendsUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoadFriendsUseCaseImpl(loadPlayerPort);
    }

    @Test
    void shouldReturnFriendListWhenPlayerAndFriendsExist() {
        UUID playerUuid = UUID.randomUUID();
        PlayerId playerId = PlayerId.of(playerUuid);

        Player player = mock(Player.class);

        UUID friendUuid = UUID.randomUUID();
        Friend friend = mock(Friend.class);
        when(friend.friendId()).thenReturn(friendUuid);
        when(player.getFriends()).thenReturn(Set.of(friend));

        //load main player
        when(loadPlayerPort.loadById(playerId)).thenReturn(Optional.of(player));

        //load friend as Player object
        Player friendPlayer = mock(Player.class);
        when(friendPlayer.getUsername()).thenReturn("john");
        when(friendPlayer.getEmail()).thenReturn("john@example.com");
        when(friendPlayer.getPictureUrl()).thenReturn("pic.jpg");
        when(friendPlayer.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(loadPlayerPort.loadById(PlayerId.of(friendUuid)))
                .thenReturn(Optional.of(friendPlayer));

        // Act
        List<Player> result = useCase.loadFriends(playerId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFriendDoesNotExist() {
        // Arrange
        UUID playerUuid = UUID.randomUUID();
        UUID friendUuid = UUID.randomUUID();

        PlayerId playerId = PlayerId.of(playerUuid);
        PlayerId friendId = PlayerId.of(friendUuid);

        Player player = mock(Player.class);
        Friend friend = new Friend(friendUuid, LocalDateTime.now());

        when(loadPlayerPort.loadById(playerId)).thenReturn(Optional.of(player));
        when(player.getFriends()).thenReturn(Set.of(friend));
        when(loadPlayerPort.loadById(friendId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> useCase.loadFriends(playerId));

        verify(loadPlayerPort).loadById(friendId);
    }

}
