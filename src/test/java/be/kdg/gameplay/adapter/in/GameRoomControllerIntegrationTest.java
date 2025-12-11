package be.kdg.gameplay.adapter.in;

import be.kdg.common.exception.GameRoomException;
import be.kdg.common.exception.GlobalExceptionHandler;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.in.request.CreateGameRoomRequest;
import be.kdg.gameplay.adapter.out.GameRoomStatusBroadcaster;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.*;
import be.kdg.gameplay.port.in.command.AcceptInvitationCommand;
import be.kdg.gameplay.port.in.command.CreateGameRoomCommand;
import be.kdg.gameplay.port.in.command.RejectInvitationCommand;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.security.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameRoomController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class GameRoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateGameRoomUseCase createGameRoomUseCase;

    @MockitoBean
    private AcceptInvitationUseCase acceptInvitationUseCase;

    @MockitoBean
    private RejectInvitationUseCase rejectInvitationUseCase;

    @MockitoBean
    private FinalizeRoomUseCase finalizeRoomUseCase;

    @MockitoBean
    private GameRoomStatusBroadcaster gameRoomStatusBroadcaster;

    @MockitoBean
    private LoadGameRoomPort loadGameRoomPort;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private UUID testPlayerId;
    private UUID testGameId;
    private UUID testRoomId;
    private UUID testInvitedPlayerId;
    private String invitedName;
    private String hostName;

    @BeforeEach
    void setUp() {
        testPlayerId = UUID.randomUUID();
        testGameId = UUID.randomUUID();
        testRoomId = UUID.randomUUID();
        testInvitedPlayerId = UUID.randomUUID();
        invitedName = UUID.randomUUID().toString();
        hostName = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreatePrivateGameRoomWithInvitation() throws Exception {
        // Arrange
        CreateGameRoomRequest request = new CreateGameRoomRequest(
                testGameId,
                testInvitedPlayerId,
                invitedName,
                "CLOSED"
        );

        GameRoom gameRoom = new GameRoom(
                GameId.of(testGameId),
                hostName,
                invitedName,
                PlayerId.of(testPlayerId),
                PlayerId.of(testInvitedPlayerId),
                GameRoomType.CLOSED
        );
        gameRoom.setStatus(GameRoomStatus.WAITING);

        when(createGameRoomUseCase.create(any(CreateGameRoomCommand.class)))
                .thenReturn(gameRoom);

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms")
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameRoomId").value(gameRoom.getGameRoomId().uuid().toString()))
                .andExpect(jsonPath("$.gameId").value(testGameId.toString()))
                .andExpect(jsonPath("$.hostPlayerId").value(testPlayerId.toString()));

        verify(createGameRoomUseCase, times(1)).create(any(CreateGameRoomCommand.class));
    }

    @Test
    void shouldReturn401WhenCreatingGameWithoutAuthentication() throws Exception {
        // Arrange
        CreateGameRoomRequest request = new CreateGameRoomRequest(
                testGameId,
                testInvitedPlayerId,
                invitedName,
                "CLOSED"
        );

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(createGameRoomUseCase, never()).create(any(CreateGameRoomCommand.class));
    }

    @Test
    void shouldHandleErrorWhenGameDoesNotExist() throws Exception {
        // Arrange
        CreateGameRoomRequest request = new CreateGameRoomRequest(
                testGameId,
                testInvitedPlayerId,
                invitedName,
                "CLOSED"
        );

        when(createGameRoomUseCase.create(any(CreateGameRoomCommand.class)))
                .thenThrow(new GameRoomException("Game not found"));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms")
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Game not found"));

        verify(createGameRoomUseCase, times(1)).create(any(CreateGameRoomCommand.class));
    }

    @Test
    void shouldHandleErrorWhenInvitedPlayerDoesNotExist() throws Exception {
        // Arrange
        CreateGameRoomRequest request = new CreateGameRoomRequest(
                testGameId,
                testInvitedPlayerId,
                invitedName,
                "CLOSED"
        );

        when(createGameRoomUseCase.create(any(CreateGameRoomCommand.class)))
                .thenThrow(new GameRoomException("Invited player not found"));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms")
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Invited player not found"));

        verify(createGameRoomUseCase, times(1)).create(any(CreateGameRoomCommand.class));
    }

    @Test
    void shouldHandleErrorWhenHostDoesntOwnGame() throws Exception {
        // Arrange
        CreateGameRoomRequest request = new CreateGameRoomRequest(
                testGameId,
                testInvitedPlayerId,
                invitedName,
                "CLOSED"
        );

        when(createGameRoomUseCase.create(any(CreateGameRoomCommand.class)))
                .thenThrow(new GameRoomException("Host does not own the game"));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms")
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Host does not own the game"));

        verify(createGameRoomUseCase, times(1)).create(any(CreateGameRoomCommand.class));
    }

    @Test
    void ShouldAcceptRoomInvitation() throws Exception {
        // Arrange
        doNothing().when(acceptInvitationUseCase).accept(any(AcceptInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(acceptInvitationUseCase, times(1)).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldReturn401WhenAcceptingInvitationWithoutAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(acceptInvitationUseCase, never()).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenGameRoomDoesNotExist() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Game room not found"))
                .when(acceptInvitationUseCase).accept(any(AcceptInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Game room not found"));

        verify(acceptInvitationUseCase, times(1)).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenPlayerIsNotFound() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Player not invited to this room"))
                .when(acceptInvitationUseCase).accept(any(AcceptInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Player not invited to this room"));

        verify(acceptInvitationUseCase, times(1)).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenRoomIsFull() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Game room is already full"))
                .when(acceptInvitationUseCase).accept(any(AcceptInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Game room is already full"));

        verify(acceptInvitationUseCase, times(1)).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenInvitationAccepted() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Invitation already accepted"))
                .when(acceptInvitationUseCase).accept(any(AcceptInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/accept", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Invitation already accepted"));

        verify(acceptInvitationUseCase, times(1)).accept(any(AcceptInvitationCommand.class));
    }

    @Test
    void shouldRejectGameRoomInvitation() throws Exception {
        // Arrange
        doNothing().when(rejectInvitationUseCase).reject(any(RejectInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(rejectInvitationUseCase, times(1)).reject(any(RejectInvitationCommand.class));
    }

    @Test
    void shouldReturn401WhenRejectingInvitationWithoutAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(rejectInvitationUseCase, never()).reject(any(RejectInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenGameRoomIsNotFound() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Game room not found"))
                .when(rejectInvitationUseCase).reject(any(RejectInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Game room not found"));

        verify(rejectInvitationUseCase, times(1)).reject(any(RejectInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenPlayerIsNotInvited() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Player not invited to this room"))
                .when(rejectInvitationUseCase).reject(any(RejectInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Player not invited to this room"));

        verify(rejectInvitationUseCase, times(1)).reject(any(RejectInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenInvitationHasAlreadyBeenRejected() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Invitation already rejected"))
                .when(rejectInvitationUseCase).reject(any(RejectInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Invitation already rejected"));

        verify(rejectInvitationUseCase, times(1)).reject(any(RejectInvitationCommand.class));
    }

    @Test
    void shouldHandleErrorWhenTryingToRejectAfterGameStarted() throws Exception {
        // Arrange
        doThrow(new GameRoomException("Cannot reject invitation after game has started"))
                .when(rejectInvitationUseCase).reject(any(RejectInvitationCommand.class));

        // Act & Assert
        mockMvc.perform(post("/api/game-rooms/{roomId}/reject", testRoomId)
                        .with(jwt().jwt(builder -> builder.subject(testPlayerId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("GameRoomError"))
                .andExpect(jsonPath("$.message").value("Cannot reject invitation after game has started"));

        verify(rejectInvitationUseCase, times(1)).reject(any(RejectInvitationCommand.class));
    }
}