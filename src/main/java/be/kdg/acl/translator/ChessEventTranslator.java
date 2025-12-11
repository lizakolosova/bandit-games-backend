package be.kdg.acl.translator;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.chess.AchievementAcquiredEvent;
import be.kdg.common.events.unified.UnifiedAchievementAchievedEvent;
import be.kdg.common.events.unified.UnifiedMatchCreatedEvent;
import be.kdg.common.events.unified.UnifiedMatchEndedEvent;
import be.kdg.common.events.unified.UnifiedMatchUpdatedEvent;
import be.kdg.common.events.chess.ChessMatchCreatedEvent;
import be.kdg.common.events.chess.ChessMatchEndedEvent;
import be.kdg.common.events.chess.ChessMatchUpdatedEvent;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.port.out.LoadGameViewProjectionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ChessEventTranslator implements EventTranslator {

    @Override
    public UnifiedMatchCreatedEvent translateToMatchCreated(DomainEvent event, GameId gameId) {
        if (!(event instanceof ChessMatchCreatedEvent chessEvent)) {
            throw new IllegalArgumentException("Cannot translate non-ChessMatchCreatedEvent");
        }

        return new UnifiedMatchCreatedEvent(
                chessEvent.gameId(),
                gameId.uuid(),
                "CHESS",
                List.of(chessEvent.whitePlayerId(), chessEvent.blackPlayerId()),
                chessEvent.messageType(),
                chessEvent.timestamp()
        );
    }

    @Override
    public UnifiedMatchUpdatedEvent translateToMatchUpdated(DomainEvent event) {
        if (!(event instanceof ChessMatchUpdatedEvent chessEvent)) {
            throw new IllegalArgumentException("Cannot translate non-ChessMatchUpdatedEvent");
        }

        return new UnifiedMatchUpdatedEvent(
                chessEvent.gameId(),
                chessEvent.whitePlayerId(),
                chessEvent.blackPlayerId(),
                chessEvent.messageType(),
                chessEvent.timestamp()
        );
    }

    @Override
    public UnifiedMatchEndedEvent translateToMatchEnded(DomainEvent event) {
        if (!(event instanceof ChessMatchEndedEvent chessEvent)) {
            throw new IllegalArgumentException("Cannot translate non-ChessMatchEndedEvent");
        }

        UUID winnerId = resolveWinnerId(chessEvent);

        return new UnifiedMatchEndedEvent(
                chessEvent.gameId(),
                winnerId,
                chessEvent.endReason(),
                chessEvent.totalMoves(),
                chessEvent.messageType(),
                chessEvent.timestamp()
        );
    }

    @Override
    public UnifiedAchievementAchievedEvent translateToAchievementAchieved(DomainEvent event) {
        if (!(event instanceof AchievementAcquiredEvent chessEvent)) {
            throw new IllegalArgumentException("Cannot translate non-AchievementAcquiredEvent");
        }

        return new UnifiedAchievementAchievedEvent(
                chessEvent.playerId(),
                null,
                chessEvent.achievementType(),
                chessEvent.gameId(),
                "CHESS",
                chessEvent.messageType(),
                chessEvent.timestamp()
        );
    }

    @Override
    public boolean canTranslate(DomainEvent event) {
        return event instanceof ChessMatchCreatedEvent ||
                event instanceof ChessMatchUpdatedEvent ||
                event instanceof ChessMatchEndedEvent ||
                event instanceof AchievementAcquiredEvent;
    }

    private UUID resolveWinnerId(ChessMatchEndedEvent event) {
        return switch (event.winner().toUpperCase()) {
            case "WHITE" -> event.whitePlayerId();
            case "BLACK" -> event.blackPlayerId();
            default -> null;
        };
    }
}