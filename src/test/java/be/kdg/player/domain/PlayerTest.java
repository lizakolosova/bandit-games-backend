package be.kdg.player.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.FriendRemovedEvent;
import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.valueobj.Friend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private UUID friend1;
    private UUID friend2;
    private UUID game1;
    private UUID game2;

    @BeforeEach
    void setUp() {
        player = new Player(
                PlayerId.create(),
                "john_doe",
                "john@example.com",
                "http://example.com/pic.png",
                java.time.LocalDateTime.now()
        );

        friend1 = UUID.randomUUID();
        friend2 = UUID.randomUUID();
        game1 = UUID.randomUUID();
        game2 = UUID.randomUUID();
    }

    @Test
    void shouldAddFriendSuccessfully() {
        // Act
        player.addFriend(friend1);

        // Assert
        assertEquals(1, player.getFriends().size());
        assertTrue(player.getFriends().stream().anyMatch(f -> f.friendId().equals(friend1)));
    }

    @Test
    void shouldRemoveFriendAndRegisterEvent() {
        // Arrange
        player.addFriend(friend1);

        // Act
        player.removeFriend(friend1);

        // Assert
        assertTrue(player.getFriends().isEmpty());

        List<DomainEvent> events = player.pullDomainEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof FriendRemovedEvent);

        FriendRemovedEvent event = (FriendRemovedEvent) events.get(0);
        assertEquals(player.getPlayerId().uuid(), event.playerId());
        assertEquals(friend1, event.friendId());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingFriend() {
        // Act + Assert
        assertThrows(NotFoundException.class, () -> player.removeFriend(friend1));
        assertTrue(player.pullDomainEvents().isEmpty());
    }

    @Test
    void shouldAddGameToLibrary() {
        // Act
        GameLibrary library = player.addGameToLibrary(game1);

        // Assert
        assertNotNull(library);
        assertEquals(game1, library.getGameId());
        assertEquals(1, player.getGameLibraries().size());
    }

    @Test
    void shouldMarkGameAsFavourite() {
        // Arrange
        player.addGameToLibrary(game1);

        // Act
        player.markGameAsFavourite(game1);

        // Assert
        GameLibrary entry = player.findGameInLibrary(game1);
        assertTrue(entry.isFavourite());
    }

    @Test
    void shouldUnmarkGameAsFavourite() {
        // Arrange
        player.addGameToLibrary(game1);
        player.markGameAsFavourite(game1);

        // Act
        player.unmarkGameAsFavourite(game1);

        // Assert
        GameLibrary entry = player.findGameInLibrary(game1);
        assertFalse(entry.isFavourite());
    }

    @Test
    void shouldThrowWhenMarkingNonExistingGameAsFavourite() {
        assertThrows(NotFoundException.class,
                () -> player.markGameAsFavourite(game1));
    }

    @Test
    void shouldThrowWhenUnmarkingNonExistingGameAsFavourite() {
        assertThrows(NotFoundException.class,
                () -> player.unmarkGameAsFavourite(game1));
    }

    @Test
    void shouldClearEventsAfterPulling() {
        // Arrange
        player.addFriend(friend1);
        player.removeFriend(friend1);

        // Act
        List<DomainEvent> firstPull = player.pullDomainEvents();
        List<DomainEvent> secondPull = player.pullDomainEvents();

        // Assert
        assertEquals(1, firstPull.size());
        assertTrue(secondPull.isEmpty());
    }
}
