package be.kdg.acl.translator;

import static org.junit.jupiter.api.Assertions.*;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.chess.*;
import be.kdg.common.events.unified.*;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.valueobj.MatchId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


class ChessEventTranslatorTest {

    private ChessEventTranslator translator;
    private final LocalDateTime now = LocalDateTime.now();
    private final MatchId matchId = MatchId.of(UUID.randomUUID());
    private final GameId gameId = GameId.of(UUID.randomUUID());
    UUID p1 = UUID.randomUUID();
    UUID p2 = UUID.randomUUID();

    @BeforeEach
    void setup() {
        translator = new ChessEventTranslator();
    }

    @Test
    void shouldTranslateToMatchCreated() {

        ChessMatchCreatedEvent event = new ChessMatchCreatedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "currentFen", "status", "message type", now);

        UnifiedMatchCreatedEvent unified = translator.translateToMatchCreated(event, gameId);

        assertEquals(List.of(p1, p2), unified.playerIds());
        assertEquals("CHESS", unified.gameType());
        assertEquals(event.messageType(), unified.messageType());
        assertEquals(event.timestamp(), unified.timestamp());
    }

    @Test
    void translateToMatchCreated_invalidType_shouldThrow() {
        DomainEvent wrong = new ChessMatchUpdatedEvent(gameId.uuid(), p1, p1.toString(), p2, p2.toString(), "currentFen",
                "status","updateType", "message type", now);

        assertThrows(IllegalArgumentException.class, () -> translator.translateToMatchCreated(wrong, gameId));
    }

    @Test
    void translateToMatchUpdated_validEvent_shouldTranslateCorrectly() {

        ChessMatchUpdatedEvent event = new ChessMatchUpdatedEvent(gameId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "currentFen", "status","updateType", "message type", now);

        UnifiedMatchUpdatedEvent unified = translator.translateToMatchUpdated(event);

        assertEquals(event.gameId(), unified.matchId());
        assertEquals(p1, unified.player1());
        assertEquals(p2, unified.player2());
        assertEquals(event.messageType(), unified.messageType());
        assertEquals(event.timestamp(), unified.timestamp());
    }

    @Test
    void translateToMatchUpdated_invalidType_shouldThrow() {
        DomainEvent wrong =  new ChessMatchCreatedEvent(matchId.uuid(), p1, p1.toString(),
                p2, p2.toString(), "currentFen", "status", "message type", now);

        assertThrows(IllegalArgumentException.class, () -> translator.translateToMatchUpdated(wrong));
    }

    @Test
    void translateToMatchEnded_whiteWins_shouldTranslateWinnerCorrectly() {

        ChessMatchEndedEvent event = new ChessMatchEndedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "FINAL FEN", "CHECKMATE", "WHITE", 42, "message type", now);


        UnifiedMatchEndedEvent unified = translator.translateToMatchEnded(event);

        assertEquals(p1, unified.winnerId());
        assertEquals("CHECKMATE", unified.endReason());
        assertEquals(42, unified.totalMoves());
    }

    @Test
    void translateToMatchEnded_blackWins_shouldTranslateWinnerCorrectly() {

        ChessMatchEndedEvent event = new ChessMatchEndedEvent(gameId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "FINAL FEN", "CHECKMATE", "BLACK", 42, "message type", now);

        UnifiedMatchEndedEvent unified = translator.translateToMatchEnded(event);

        assertEquals(p2, unified.winnerId());
        assertEquals("CHECKMATE", unified.endReason());
        assertEquals(42, unified.totalMoves());
    }

    @Test
    void translateToMatchEnded_draw_shouldSetWinnerToNull() {

        ChessMatchEndedEvent event = new ChessMatchEndedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(), "FINAL FEN",
                "CHECKMATE", "DRAW", 42, "message type", now);


        UnifiedMatchEndedEvent unified = translator.translateToMatchEnded(event);

        assertNull(unified.winnerId());
    }

    @Test
    void translateToMatchEnded_invalidType_shouldThrow() {
        DomainEvent wrong = new ChessMatchUpdatedEvent(
                matchId.uuid(), p1, p1.toString(), p2, p2.toString(), "currentFen", "status","updateType",
                "message type", now);

        assertThrows(IllegalArgumentException.class,
                () -> translator.translateToMatchEnded(wrong));
    }


    @Test
    void translateToAchievementAchieved_validEvent_shouldTranslateCorrectly() {
        AchievementAcquiredEvent event = new AchievementAcquiredEvent(matchId.uuid(), p1, p1.toString(), "FIRST_BLOOD",
                "CHESS_ACHIEVEMENT", "blah blah", now);

        UnifiedAchievementAchievedEvent unified = translator.translateToAchievementAchieved(event);

        assertEquals(p1, unified.playerId());
        assertEquals("FIRST_BLOOD", unified.achievementType());
        assertEquals("CHESS", unified.gameType());
        assertEquals(event.timestamp(), unified.timestamp());
    }

    @Test
    void translateToAchievementAchieved_invalidType_shouldThrow() {
        DomainEvent wrong = new ChessMatchCreatedEvent(matchId.uuid(), p1, p1.toString(),
                p2, p2.toString(), "currentFen", "status", "message type", now);

        assertThrows(IllegalArgumentException.class, () -> translator.translateToAchievementAchieved(wrong));
    }

    @Test
    void canTranslate_shouldReturnTrueForChessEvents() {
        assertTrue(translator.canTranslate(new ChessMatchCreatedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "currentFen", "status", "message type", now)));
        assertTrue(translator.canTranslate(new ChessMatchUpdatedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "currentFen", "status","updateType", "message type", now)));
        assertTrue(translator.canTranslate(new ChessMatchEndedEvent(matchId.uuid(), p1, p1.toString(), p2, p2.toString(),
                "FINAL FEN", "CHECKMATE", "BLACK", 42, "message type", now)));
        assertTrue(translator.canTranslate( new AchievementAcquiredEvent(matchId.uuid(), p1, p1.toString(), "FIRST_BLOOD",
                "CHESS_ACHIEVEMENT", "blah blah", now)));
    }
}
