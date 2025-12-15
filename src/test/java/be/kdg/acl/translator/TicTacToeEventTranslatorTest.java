package be.kdg.acl.translator;

import static org.junit.jupiter.api.Assertions.*;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.tictactoe.*;
import be.kdg.common.events.unified.*;
import be.kdg.common.valueobj.GameId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class TicTacToeEventTranslatorTest {

    private TicTacToeEventTranslator translator;

    UUID matchId = UUID.randomUUID();
    UUID gameIdUuid = UUID.randomUUID();
    UUID p1 = UUID.randomUUID();
    UUID p2 = UUID.randomUUID();
    UUID achievementId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();
    GameId gameId = GameId.of(UUID.randomUUID());

    @BeforeEach
    void setup() {
        translator = new TicTacToeEventTranslator();
    }

    @Test
    void shouldTranslateMatchCreatedCorrectly() {
        TicTacToeMatchCreatedEvent event = new TicTacToeMatchCreatedEvent(
                matchId, gameIdUuid, p1, p2, List.of("", "", "", "", "", "", "", "", ""),
                "ACTIVE", "message type", now
        );

        UnifiedMatchCreatedEvent unified =
                translator.translateToMatchCreated(event, gameId);

        assertEquals(matchId, unified.matchId());
        assertEquals(gameId.uuid(), unified.gameId());
        assertEquals("TICTACTOE", unified.gameType());
        assertEquals(List.of(p1, p2), unified.playerIds());
        assertEquals("message type", unified.messageType());
        assertEquals(now, unified.timestamp());
    }

    @Test
    void translateMatchCreatedShouldFailForWrongEvent() {
        DomainEvent wrong = new TicTacToeMatchEndedEvent(
                matchId, gameIdUuid, p1, p2, List.of("X", "O", "X", "", "", "", "", "", ""),
                "WIN", p1, 3, "endType", now
        );

        assertThrows(IllegalArgumentException.class,
                () -> translator.translateToMatchCreated(wrong, gameId));
    }

    @Test
    void shouldTranslateMatchUpdatedCorrectly() {
        TicTacToeMatchMoveMadeEvent event = new TicTacToeMatchMoveMadeEvent(
                matchId, gameIdUuid, p1, 4, "X", 1,
                List.of("", "", "", "", "X", "", "", "", ""),
                p1, p2, now, "message type", now
        );

        UnifiedMatchUpdatedEvent unified =
                translator.translateToMatchUpdated(event);

        assertEquals(matchId, unified.matchId());
        assertEquals(p1, unified.player1());
        assertEquals(p1, unified.player2());
        assertEquals("message type", unified.messageType());
        assertEquals(now, unified.timestamp());
    }

    @Test
    void translateMatchUpdatedShouldFailForWrongEvent() {
        DomainEvent wrong = new TicTacToeMatchCreatedEvent(
                matchId, gameIdUuid, p1, p2, List.of("", "", "", "", "", "", "", "", ""),
                "ACTIVE", "message", now
        );

        assertThrows(IllegalArgumentException.class,
                () -> translator.translateToMatchUpdated(wrong));
    }

    @Test
    void shouldTranslateMatchEndedCorrectly() {
        TicTacToeMatchEndedEvent event = new TicTacToeMatchEndedEvent(
                matchId, gameIdUuid, p1, p2,
                List.of("X", "X", "X", "O", "O", "", "", "", ""),
                "WIN", p1, 5, "message type", now
        );

        UnifiedMatchEndedEvent unified = translator.translateToMatchEnded(event);

        assertEquals(matchId, unified.matchId());
        assertEquals(p1, unified.winnerId());
        assertEquals("WIN", unified.endReason());
        assertEquals(5, unified.totalMoves());
        assertEquals("message type", unified.messageType());
        assertEquals(now, unified.timestamp());
    }

    @Test
    void translateMatchEndedShouldFailForWrongEvent() {
        DomainEvent wrong = new TicTacToeMatchMoveMadeEvent(
                matchId, gameIdUuid, p1, 0, "X", 1,
                List.of("X", "", "", "", "", "", "", "", ""),
                p1, p2, now, "msg", now
        );

        assertThrows(IllegalArgumentException.class,
                () -> translator.translateToMatchEnded(wrong));
    }

    @Test
    void shouldTranslateAchievementCorrectly() {
        TicTacToeAchievementAchievedEvent event = new TicTacToeAchievementAchievedEvent(
                p1, achievementId, "ACH_TYPE", matchId, gameId.uuid(), "msg", now
        );

        UnifiedAchievementAchievedEvent unified = translator.translateToAchievementAchieved(event);

        assertEquals(p1, unified.playerId());
        assertEquals(achievementId, unified.achievementId());
        assertEquals("ACH_TYPE", unified.achievementType());
        assertEquals(matchId, unified.matchId());
        assertEquals("TICTACTOE", unified.gameType());
        assertEquals("msg", unified.messageType());
        assertEquals(now, unified.timestamp());
    }

    @Test
    void translateToAchievementShouldFailForWrongEvent() {
        DomainEvent wrong = new TicTacToeMatchCreatedEvent(
                matchId, gameIdUuid, p1, p2, List.of("", "", "", "", "", "", "", "", ""),
                "ACTIVE", "msg", now
        );

        assertThrows(IllegalArgumentException.class,
                () -> translator.translateToAchievementAchieved(wrong));
    }

    @Test
    void shouldReturnTrueForSupportedEvents() {
        assertTrue(translator.canTranslate(
                new TicTacToeMatchCreatedEvent(matchId, gameIdUuid, p1, p2,
                        List.of("", "", "", "", "", "", "", "", ""), "ACTIVE", "msg", now)));
        assertTrue(translator.canTranslate(
                new TicTacToeMatchMoveMadeEvent(matchId, gameIdUuid, p1, 0, "X", 1,
                        List.of("X", "", "", "", "", "", "", "", ""), p1, p2, now, "msg", now)));
        assertTrue(translator.canTranslate(
                new TicTacToeMatchEndedEvent(matchId, gameIdUuid, p1, p2,
                        List.of("X", "X", "X", "O", "O", "", "", "", ""), "WIN", p1, 4, "msg", now)));
        assertTrue(translator.canTranslate(
                new TicTacToeAchievementAchievedEvent(p1, achievementId, "TYPE", matchId,
                        gameId.uuid(), "msg", now)));
    }
}