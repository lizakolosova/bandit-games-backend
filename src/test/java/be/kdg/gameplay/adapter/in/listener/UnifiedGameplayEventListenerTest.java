package be.kdg.gameplay.adapter.in.listener;


import be.kdg.acl.translator.ChessEventTranslator;
import be.kdg.acl.translator.TicTacToeEventTranslator;
import be.kdg.common.events.chess.*;
import be.kdg.common.events.tictactoe.*;
import be.kdg.common.events.unified.UnifiedMatchCreatedEvent;
import be.kdg.common.events.unified.UnifiedMatchEndedEvent;
import be.kdg.common.events.unified.UnifiedMatchUpdatedEvent;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.adapter.out.GameRoomStatusBroadcaster;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.UnifiedMatchProjector;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.LoadGameViewProjectionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnifiedGameplayEventListenerTest {

    @Mock UnifiedMatchProjector projector;
    @Mock ChessEventTranslator chessTranslator;
    @Mock TicTacToeEventTranslator tttTranslator;
    @Mock LoadGameViewProjectionPort loadGameViewProjectionPort;
    @Mock GameRoomStatusBroadcaster broadcaster;
    @Mock LoadGameRoomPort loadGameRoomPort;

    UUID matchId = UUID.randomUUID();
    UUID gameId = UUID.randomUUID();
    UUID p1 = UUID.randomUUID();
    UUID p2 = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    UnifiedGameplayEventListener listener;

    @BeforeEach
    void setup() {
        listener = new UnifiedGameplayEventListener(
                projector, chessTranslator, tttTranslator,
                loadGameViewProjectionPort, broadcaster, loadGameRoomPort
        );
    }

    @Test
    void shouldProjectCreatedEventAndBroadcast() {

        // ARRANGE
        ChessMatchCreatedEvent event = new ChessMatchCreatedEvent(matchId, p1, p1.toString(),
                p2, p2.toString(), "currentFen", "status", "message type", now);

        GameViewProjection view = new GameViewProjection(
                GameId.of(UUID.randomUUID()),
                "CHESS"
        );

        UnifiedMatchCreatedEvent unified = new UnifiedMatchCreatedEvent(
                matchId,
                view.getGameId().uuid(),
                "CHESS",
                List.of(p1, p2),
                "random message",
                now
        );

        GameRoom room = mock(GameRoom.class);
        when(room.getGameRoomId()).thenReturn(new GameRoomId(UUID.randomUUID()));

        when(loadGameViewProjectionPort.findByName("CHESS")).thenReturn(view);
        when(chessTranslator.translateToMatchCreated(event, view.getGameId())).thenReturn(unified);
        when(loadGameRoomPort.findByPlayers(p1, p2)).thenReturn(room);

        // ACT
        listener.onChessMatchCreated(event);

        // ASSERT
        verify(projector).project(any(UnifiedMatchCreatedProjectionCommand.class));
        verify(broadcaster).broadcastMatchStarted(
                room.getGameRoomId().uuid().toString(),
                matchId.toString()
        );
    }

    @Test
    void shouldProjectChessMatchUpdated() {

        // ARRANGE
        ChessMatchUpdatedEvent event = new ChessMatchUpdatedEvent(
                gameId, p1, p1.toString(), p2, p2.toString(), "currentFen", "status","updateType",
                "message type", now);

        UnifiedMatchUpdatedEvent unified = new UnifiedMatchUpdatedEvent(
                gameId, p1, p2,"message type", now
        );

        when(chessTranslator.translateToMatchUpdated(event)).thenReturn(unified);

        // ACT
        listener.onChessMatchUpdated(event);

        // ASSERT
        verify(projector).project(any(UnifiedMatchUpdatedProjectionCommand.class));
        verifyNoInteractions(broadcaster);
    }

    @Test
    void shouldProjectChessMatchEndedEvent() {

        // ARRANGE

        ChessMatchEndedEvent event = new ChessMatchEndedEvent(
                gameId, p1, p1.toString(), p2, p2.toString(), "FINAL FEN", "CHECKMATE", "BLACK",
                42, "message type", now);

        UnifiedMatchEndedEvent unified = new UnifiedMatchEndedEvent(
                gameId, p2, "CHECKMATE", 42,"message type", now);

        when(chessTranslator.translateToMatchEnded(event)).thenReturn(unified);

        // ACT
        listener.onChessMatchEnded(event);

        // ASSERT
        verify(projector).project(any(UnifiedMatchEndedProjectionCommand.class));
        verifyNoInteractions(broadcaster);
    }

    @Test
    void shouldProjectAndBroadcastTicTacToeMatchCreated() {

        TicTacToeMatchCreatedEvent event = new TicTacToeMatchCreatedEvent(
                matchId, gameId, p1, p2,
                List.of("", "", "", "", "", "", "", "", ""),
                "ACTIVE", "message type", now
        );

        UnifiedMatchCreatedEvent unified = new UnifiedMatchCreatedEvent(
                matchId, UUID.randomUUID(), "TIC_TAC_TOE", List.of(p1, p2), "message type", now);

        GameRoom room = mock(GameRoom.class);
        when(room.getGameRoomId()).thenReturn(new GameRoomId(UUID.randomUUID()));

        when(tttTranslator.translateToMatchCreated(eq(event), any())).thenReturn(unified);
        when(loadGameRoomPort.findByPlayers(p1, p2)).thenReturn(room);

        listener.onTicTacToeMatchCreated(event);

        verify(projector).project(any(UnifiedMatchCreatedProjectionCommand.class));
        verify(broadcaster).broadcastMatchStarted(
                room.getGameRoomId().uuid().toString(),
                matchId.toString()
        );
    }

    @Test
    void shouldProjectTicTacToeMatchEndedEvent() {

        TicTacToeMatchEndedEvent event = new TicTacToeMatchEndedEvent(
                matchId, gameId, p1, p2,
                List.of("X", "X", "X", "O", "O", "", "", "", ""),
                "WIN", p1, 5, "message type", now
        );

        UnifiedMatchEndedEvent unified = new UnifiedMatchEndedEvent(
                matchId, p1, "WIN", 5,"messageType", now);

        when(tttTranslator.translateToMatchEnded(event)).thenReturn(unified);

        listener.onTicTacToeMatchEnded(event);

        verify(projector).project(any(UnifiedMatchEndedProjectionCommand.class));
        verifyNoInteractions(broadcaster);
    }

    @Test
    void shouldFailWhenGameViewMissing() {
        ChessMatchCreatedEvent event =
                new ChessMatchCreatedEvent(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID().toString(),"currentFen", "status", "message type", LocalDateTime.now());

        when(loadGameViewProjectionPort.findByName("CHESS")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> listener.onChessMatchCreated(event));
        verifyNoInteractions(projector);
    }

    @Test
    void shouldFailWhenRoomIsMissing() {
        TicTacToeMatchCreatedEvent event = new TicTacToeMatchCreatedEvent(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                List.of("", "", "", "", "", "", "", "", ""),
                "ACTIVE", "message type", LocalDateTime.now()
        );

        UnifiedMatchCreatedEvent unified = new UnifiedMatchCreatedEvent(event.matchId(), UUID.randomUUID(), "TIC_TAC_TOE",
                List.of(event.hostPlayerId(), event.opponentPlayerId()),"message type", LocalDateTime.now());

        when(tttTranslator.translateToMatchCreated(any(), any())).thenReturn(unified);
        when(loadGameRoomPort.findByPlayers(any(), any())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> listener.onTicTacToeMatchCreated(event));
        verify(projector).project(any(UnifiedMatchCreatedProjectionCommand.class));
    }
}