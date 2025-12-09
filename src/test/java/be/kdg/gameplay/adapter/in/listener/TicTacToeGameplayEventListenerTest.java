package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.events.tictactoe.TicTacToeMatchCreatedEvent;
import be.kdg.common.events.tictactoe.TicTacToeMatchUpdatedEvent;
import be.kdg.common.events.tictactoe.TicTacToeMatchEndedEvent;
import be.kdg.gameplay.adapter.out.GameRoomStatusBroadcaster;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.TicTacToeGameProjector;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicTacToeGameplayEventListenerTest {

    @Mock
    private TicTacToeGameProjector projector;

    @Mock
    private GameRoomStatusBroadcaster broadcaster;

    @Mock
    private LoadGameRoomPort loadGameRoomPort;

    private TicTacToeGameplayEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new TicTacToeGameplayEventListener(projector, broadcaster, loadGameRoomPort);
    }

    @Test
    void shouldProjectGameCreatedEvent() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID oppId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        TicTacToeMatchCreatedEvent event = new TicTacToeMatchCreatedEvent(
                matchId, hostId, oppId, "GAME_CREATED", ts
        );

        ArgumentCaptor<TicTacToeGameCreatedProjectionCommand> captor =
                ArgumentCaptor.forClass(TicTacToeGameCreatedProjectionCommand.class);

        GameRoom mockGameRoom = mock(GameRoom.class);
        when(mockGameRoom.getGameRoomId()).thenReturn(GameRoomId.of(UUID.randomUUID()));

        when(loadGameRoomPort.findByPlayers(hostId, oppId))
                .thenReturn(mockGameRoom);

        // Act
        listener.onGameCreated(event);

        // Assert
        verify(projector).project(captor.capture());
        TicTacToeGameCreatedProjectionCommand cmd = captor.getValue();

        assertEquals(matchId, cmd.matchId());
        assertEquals(hostId, cmd.hostPlayerId());
        assertEquals(oppId, cmd.opponentPlayerId());
        assertEquals(ts, cmd.timestamp());
    }

    @Test
    void shouldProjectGameUpdatedEvent() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        TicTacToeMatchUpdatedEvent event = new TicTacToeMatchUpdatedEvent(
                matchId, playerId, 4, 3,
                List.of("X", "O", "-", "-", "-", "-", "-", "-", "-"),
                "MOVE_MADE", ts
        );

        ArgumentCaptor<TicTacToeGameUpdatedProjectionCommand> captor =
                ArgumentCaptor.forClass(TicTacToeGameUpdatedProjectionCommand.class);

        // Act
        listener.onGameUpdated(event);

        // Assert
        verify(projector).project(captor.capture());
        TicTacToeGameUpdatedProjectionCommand cmd = captor.getValue();

        assertEquals(matchId, cmd.matchId());
        assertEquals(playerId, cmd.playerId());
        assertEquals(4, cmd.moveNumber());
        assertEquals(3, cmd.position());
        assertEquals(event.boardStateAfterMove(), cmd.boardState());
        assertEquals(ts, cmd.timestamp());
    }

    @Test
    void shouldProjectGameEndedEvent() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        TicTacToeMatchEndedEvent event = new TicTacToeMatchEndedEvent(
                matchId, winnerId, "WIN", 5,
                List.of("X", "X", "X", "O", "O", "-", "-", "-", "-"),
                "GAME_ENDED", ts
        );

        ArgumentCaptor<TicTacToeGameEndedProjectionCommand> captor =
                ArgumentCaptor.forClass(TicTacToeGameEndedProjectionCommand.class);

        // Act
        listener.onGameEnded(event);

        // Assert
        verify(projector).project(captor.capture());
        TicTacToeGameEndedProjectionCommand cmd = captor.getValue();

        assertEquals(matchId, cmd.matchId());
        assertEquals(winnerId, cmd.winnerId());
        assertEquals("WIN", cmd.endReason());
        assertEquals(5, cmd.totalMoves());
        assertEquals(ts, cmd.timestamp());
    }

    @Test
    void shouldProjectGameEndedEventWithNullWinner() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        TicTacToeMatchEndedEvent event = new TicTacToeMatchEndedEvent(
                matchId, null, "DRAW", 9,
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-"),
                "GAME_ENDED", ts
        );

        ArgumentCaptor<TicTacToeGameEndedProjectionCommand> captor =
                ArgumentCaptor.forClass(TicTacToeGameEndedProjectionCommand.class);

        // Act
        listener.onGameEnded(event);

        // Assert
        verify(projector).project(captor.capture());
        TicTacToeGameEndedProjectionCommand cmd = captor.getValue();

        assertNull(cmd.winnerId());
        assertEquals("DRAW", cmd.endReason());
        assertEquals(9, cmd.totalMoves());
    }

    @Test
    void shouldNotFailWhenEventContainsNullFields() {
        // Arrange
        TicTacToeMatchEndedEvent event = new TicTacToeMatchEndedEvent(
                null, null, null, 0,
                null, null, null
        );

        // Act
        assertDoesNotThrow(() -> listener.onGameEnded(event));
        verify(projector).project((TicTacToeGameEndedProjectionCommand) any());
    }
}
