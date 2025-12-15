package be.kdg.player.adapter.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.request.AddGameToLibraryRequest;
import be.kdg.player.adapter.in.request.RegisterPlayerRequest;
import be.kdg.player.adapter.in.request.SendFriendRequestDto;
import be.kdg.player.adapter.in.response.*;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.*;
import be.kdg.player.port.in.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private LoadGameLibraryUseCase loadGameLibraryUseCase;

    @Mock
    private LoadLibraryGameUseCase loadLibraryGameUseCase;

    @Mock
    private LoadFriendsUseCase loadFriendsUseCase;

    @Mock
    private RegisterPlayerUseCase registerPlayerUseCase;

    @Mock
    private MarkFavouriteUseCase markFavouriteUseCase;

    @Mock
    private SendFriendshipRequestUseCase sendFriendshipRequestUseCase;

    @Mock
    private SearchPlayersUseCase searchPlayersUseCase;

    @Mock
    private RemoveFriendUseCase removeFriendUseCase;

    @InjectMocks
    private PlayerController playerController;

    private Jwt mockJwt;
    private UUID testPlayerId;
    private UUID testGameId;

    @BeforeEach
    void setUp() {
        testPlayerId = UUID.randomUUID();
        testGameId = UUID.randomUUID();
        mockJwt = createMockJwt(testPlayerId.toString());
    }

    private Jwt createMockJwt(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject)
                .build();
    }

    @Test
    void ShouldReturnCreatedPlayerWhenPlayerIsRegistered() {
        // Arrange
        RegisterPlayerRequest request = new RegisterPlayerRequest("testuser", "test@example.com", "blah blah", "url");
        Player mockPlayer = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        when(mockPlayer.getPlayerId()).thenReturn(PlayerId.of(playerId));
        when(mockPlayer.getUsername()).thenReturn("testuser");
        when(mockPlayer.getEmail()).thenReturn("test@example.com");
        when(mockPlayer.getPictureUrl()).thenReturn("http://picture.url");
        when(mockPlayer.getCreatedAt()).thenReturn(createdAt);
        when(registerPlayerUseCase.register(any(RegisterPlayerCommand.class))).thenReturn(mockPlayer);

        // Act
        ResponseEntity<PlayerDto> response = playerController.registerPlayer(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playerId, response.getBody().playerId());
        assertEquals("testuser", response.getBody().username());
        assertEquals("test@example.com", response.getBody().email());
        verify(registerPlayerUseCase, times(1)).register(any(RegisterPlayerCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringWithInvalidData() {
        // Arrange
        RegisterPlayerRequest request = new RegisterPlayerRequest("", "invalid-email", "", "");
        when(registerPlayerUseCase.register(any(RegisterPlayerCommand.class)))
                .thenThrow(new IllegalArgumentException("Invalid player data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            playerController.registerPlayer(request);
        });
        verify(registerPlayerUseCase, times(1)).register(any(RegisterPlayerCommand.class));
    }

    @Test
    void ShouldThrowExceptionWhenRegisteringDuplicateUsername() {
        // Arrange
        RegisterPlayerRequest request = new RegisterPlayerRequest("existinguser", "new@example.com","PASSWORD", "URL");
        when(registerPlayerUseCase.register(any(RegisterPlayerCommand.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.registerPlayer(request);
        });
        verify(registerPlayerUseCase, times(1)).register(any(RegisterPlayerCommand.class));
    }


    @Test
    void shouldLoadGameLibrary() {
        // Arrange
        GameLibrary mockGameLibrary = mock(GameLibrary.class);
        when(mockGameLibrary.getGameId()).thenReturn(testGameId);
        when(mockGameLibrary.getPurchasedAt()).thenReturn(LocalDateTime.now());
        when(mockGameLibrary.getLastPlayedAt()).thenReturn(LocalDateTime.now());
        when(mockGameLibrary.getTotalPlaytime()).thenReturn(Duration.ofMinutes(120));
        when(mockGameLibrary.isFavourite()).thenReturn(true);

        when(loadGameLibraryUseCase.loadLibrary(any(LoadGameLibraryCommand.class)))
                .thenReturn(List.of(mockGameLibrary));

        // Act
        ResponseEntity<List<GameLibraryDto>> response = playerController.loadLibrary(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testGameId, response.getBody().get(0).gameId());
        assertEquals(120, response.getBody().get(0).totalPlaytimeMinutes());
        assertTrue(response.getBody().get(0).favourite());
        verify(loadGameLibraryUseCase, times(1)).loadLibrary(any(LoadGameLibraryCommand.class));
    }

    @Test
    void shouldReturnEmptyListWhenLoadLibraryIsEmpty() {
        // Arrange
        when(loadGameLibraryUseCase.loadLibrary(any(LoadGameLibraryCommand.class)))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<GameLibraryDto>> response = playerController.loadLibrary(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(loadGameLibraryUseCase, times(1)).loadLibrary(any(LoadGameLibraryCommand.class));
    }

    @Test
    void shouldHandleNullPlaytime() {
        // Arrange
        GameLibrary mockGameLibrary = mock(GameLibrary.class);
        when(mockGameLibrary.getGameId()).thenReturn(testGameId);
        when(mockGameLibrary.getPurchasedAt()).thenReturn(LocalDateTime.now());
        when(mockGameLibrary.getLastPlayedAt()).thenReturn(null);
        when(mockGameLibrary.getTotalPlaytime()).thenReturn(null);
        when(mockGameLibrary.isFavourite()).thenReturn(false);

        when(loadGameLibraryUseCase.loadLibrary(any(LoadGameLibraryCommand.class)))
                .thenReturn(List.of(mockGameLibrary));

        // Act
        ResponseEntity<List<GameLibraryDto>> response = playerController.loadLibrary(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getBody().get(0).totalPlaytimeMinutes());
        verify(loadGameLibraryUseCase, times(1)).loadLibrary(any(LoadGameLibraryCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenGameNotInLibrary() {
        // Arrange
        when(loadLibraryGameUseCase.loadGame(any(LoadLibraryGameCommand.class)))
                .thenThrow(new RuntimeException("Game not found in library"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.loadLibraryGame(testGameId, mockJwt);
        });
        verify(loadLibraryGameUseCase, times(1)).loadGame(any(LoadLibraryGameCommand.class));
    }


    @Test
    void shouldLoadFriendsList() {
        // Arrange
        Player mockFriend = mock(Player.class);
        UUID friendId = UUID.randomUUID();
        when(mockFriend.getPlayerId()).thenReturn(PlayerId.of(friendId));
        when(mockFriend.getUsername()).thenReturn("friend1");
        when(mockFriend.getPictureUrl()).thenReturn("http://picture.url");

        when(loadFriendsUseCase.loadFriends(any(PlayerId.class)))
                .thenReturn(List.of(mockFriend));

        // Act
        ResponseEntity<List<FriendDto>> response = playerController.loadFriends(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(friendId, response.getBody().get(0).friendId());
        assertEquals("friend1", response.getBody().get(0).username());
        verify(loadFriendsUseCase, times(1)).loadFriends(any(PlayerId.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoFriends() {
        // Arrange
        when(loadFriendsUseCase.loadFriends(any(PlayerId.class)))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<FriendDto>> response = playerController.loadFriends(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(loadFriendsUseCase, times(1)).loadFriends(any(PlayerId.class));
    }

    @Test
    void shouldMarkGameAsFavourite() {
        // Arrange
        doNothing().when(markFavouriteUseCase).markFavourite(any(MarkFavouriteCommand.class));

        // Act
        ResponseEntity<Void> response = playerController.toggleFavourite(testGameId, mockJwt, true);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(markFavouriteUseCase, times(1)).markFavourite(any(MarkFavouriteCommand.class));
    }

    @Test
    void shouldUnmarkGameAsFavourite() {
        // Arrange
        doNothing().when(markFavouriteUseCase).markFavourite(any(MarkFavouriteCommand.class));

        // Act
        ResponseEntity<Void> response = playerController.toggleFavourite(testGameId, mockJwt, false);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(markFavouriteUseCase, times(1)).markFavourite(any(MarkFavouriteCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenGameIsNotInLibrary() {
        // Arrange
        doThrow(new RuntimeException("Game not found in library"))
                .when(markFavouriteUseCase).markFavourite(any(MarkFavouriteCommand.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.toggleFavourite(testGameId, mockJwt, true);
        });
        verify(markFavouriteUseCase, times(1)).markFavourite(any(MarkFavouriteCommand.class));
    }

    @Test
    void shouldSendFriendRequest() {
        // Arrange
        UUID receiverId = UUID.randomUUID();
        SendFriendRequestDto request = new SendFriendRequestDto(receiverId);
        when((sendFriendshipRequestUseCase).sendRequest(any(SendFriendshipRequestCommand.class))).thenReturn(null);

        // Act
        ResponseEntity<Void> response = playerController.sendFriendRequest(mockJwt, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sendFriendshipRequestUseCase, times(1)).sendRequest(any(SendFriendshipRequestCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenSendFriendshipRequestToSelf() {
        // Arrange
        SendFriendRequestDto request = new SendFriendRequestDto(testPlayerId);
        doThrow(new IllegalArgumentException("Cannot send friend request to yourself"))
                .when(sendFriendshipRequestUseCase).sendRequest(any(SendFriendshipRequestCommand.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            playerController.sendFriendRequest(mockJwt, request);
        });
        verify(sendFriendshipRequestUseCase, times(1)).sendRequest(any(SendFriendshipRequestCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenReceiverDoesNotExist() {
        // Arrange
        UUID receiverId = UUID.randomUUID();
        SendFriendRequestDto request = new SendFriendRequestDto(receiverId);
        doThrow(new RuntimeException("Receiver not found"))
                .when(sendFriendshipRequestUseCase).sendRequest(any(SendFriendshipRequestCommand.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.sendFriendRequest(mockJwt, request);
        });
        verify(sendFriendshipRequestUseCase, times(1)).sendRequest(any(SendFriendshipRequestCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenFriendshipAlreadyExists() {
        // Arrange
        UUID receiverId = UUID.randomUUID();
        SendFriendRequestDto request = new SendFriendRequestDto(receiverId);
        doThrow(new RuntimeException("Already friends"))
                .when(sendFriendshipRequestUseCase).sendRequest(any(SendFriendshipRequestCommand.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.sendFriendRequest(mockJwt, request);
        });
        verify(sendFriendshipRequestUseCase, times(1)).sendRequest(any(SendFriendshipRequestCommand.class));
    }

    @Test
    void shouldSearchPlayersWithQuery() {
        // Arrange
        Player mockPlayer = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(mockPlayer.getPlayerId()).thenReturn(PlayerId.of(playerId));
        when(mockPlayer.getUsername()).thenReturn("searchuser");
        when(mockPlayer.getEmail()).thenReturn("search@example.com");
        when(mockPlayer.getPictureUrl()).thenReturn("http://picture.url");
        when(mockPlayer.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(searchPlayersUseCase.search(any(SearchPlayersCommand.class)))
                .thenReturn(List.of(mockPlayer));

        // Act
        ResponseEntity<List<PlayerDto>> response = playerController.searchPlayers(mockJwt, "search");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("searchuser", response.getBody().get(0).username());
        verify(searchPlayersUseCase, times(1)).search(any(SearchPlayersCommand.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoPlayersMatchQuery() {
        // Arrange
        when(searchPlayersUseCase.search(any(SearchPlayersCommand.class)))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<PlayerDto>> response = playerController.searchPlayers(mockJwt, "nonexistent");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(searchPlayersUseCase, times(1)).search(any(SearchPlayersCommand.class));
    }

    @Test
    void shouldSearchPlayersWithNullQuery() {
        // Arrange
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        when(mockPlayer1.getPlayerId()).thenReturn(PlayerId.of(UUID.randomUUID()));
        when(mockPlayer1.getUsername()).thenReturn("user1");
        when(mockPlayer1.getEmail()).thenReturn("user1@example.com");
        when(mockPlayer1.getPictureUrl()).thenReturn("http://picture1.url");
        when(mockPlayer1.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(mockPlayer2.getPlayerId()).thenReturn(PlayerId.of(UUID.randomUUID()));
        when(mockPlayer2.getUsername()).thenReturn("user2");
        when(mockPlayer2.getEmail()).thenReturn("user2@example.com");
        when(mockPlayer2.getPictureUrl()).thenReturn("http://picture2.url");
        when(mockPlayer2.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(searchPlayersUseCase.search(any(SearchPlayersCommand.class)))
                .thenReturn(Arrays.asList(mockPlayer1, mockPlayer2));

        // Act
        ResponseEntity<List<PlayerDto>> response = playerController.searchPlayers(mockJwt, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(searchPlayersUseCase, times(1)).search(any(SearchPlayersCommand.class));
    }

    @Test
    void shouldRemoveFriend() {
        // Arrange
        UUID friendId = UUID.randomUUID();
        doNothing().when(removeFriendUseCase).removeFriend(any(RemoveFriendCommand.class));

        // Act
        ResponseEntity<Void> response = playerController.removeFriend(mockJwt, friendId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(removeFriendUseCase, times(1)).removeFriend(any(RemoveFriendCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenFriendshipDoesNotExist() {
        // Arrange
        UUID friendId = UUID.randomUUID();
        doThrow(new RuntimeException("Friendship not found"))
                .when(removeFriendUseCase).removeFriend(any(RemoveFriendCommand.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerController.removeFriend(mockJwt, friendId);
        });
        verify(removeFriendUseCase, times(1)).removeFriend(any(RemoveFriendCommand.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToRemoveSelf() {
        // Arrange
        doThrow(new IllegalArgumentException("Cannot remove yourself as friend"))
                .when(removeFriendUseCase).removeFriend(any(RemoveFriendCommand.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            playerController.removeFriend(mockJwt, testPlayerId);
        });
        verify(removeFriendUseCase, times(1)).removeFriend(any(RemoveFriendCommand.class));
    }
}