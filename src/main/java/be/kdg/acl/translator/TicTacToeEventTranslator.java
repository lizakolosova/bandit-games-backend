package be.kdg.acl.translator;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.tictactoe.*;
import be.kdg.common.events.unified.*;
import be.kdg.common.valueobj.GameId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TicTacToeEventTranslator implements EventTranslator {

    @Override
    public UnifiedMatchCreatedEvent translateToMatchCreated(DomainEvent event, GameId gameId) {
        if (!(event instanceof TicTacToeMatchCreatedEvent tttEvent)) {
            throw new IllegalArgumentException("Cannot translate non-TicTacToeMatchCreatedEvent");
        }

        return new UnifiedMatchCreatedEvent(
                tttEvent.matchId(),
                gameId.uuid(),
                "TICTACTOE",
                List.of(tttEvent.hostPlayerId(), tttEvent.opponentPlayerId()),
                tttEvent.messageType(),
                tttEvent.timestamp()
        );
    }

    @Override
    public UnifiedMatchUpdatedEvent translateToMatchUpdated(DomainEvent event) {
        if (!(event instanceof TicTacToeMatchMoveMadeEvent tttEvent)) {
            throw new IllegalArgumentException("Cannot translate non-TicTacToeMatchMoveMadeEvent");
        }

        return new UnifiedMatchUpdatedEvent(
                tttEvent.matchId(),
                tttEvent.playerId(),
                tttEvent.playerId(),
                tttEvent.messageType(),
                tttEvent.timestamp()
        );
    }

    @Override
    public UnifiedMatchEndedEvent translateToMatchEnded(DomainEvent event) {
        if (!(event instanceof TicTacToeMatchEndedEvent tttEvent)) {
            throw new IllegalArgumentException("Cannot translate non-TicTacToeMatchEndedEvent");
        }

        return new UnifiedMatchEndedEvent(
                tttEvent.matchId(),
                tttEvent.winnerId(),
                tttEvent.endReason(),
                tttEvent.totalMoves(),
                tttEvent.messageType(),
                tttEvent.timestamp()
        );
    }

    @Override
    public UnifiedAchievementAchievedEvent translateToAchievementAchieved(DomainEvent event) {
        if (!(event instanceof TicTacToeAchievementAchievedEvent tttEvent)) {
            throw new IllegalArgumentException("Cannot translate non-TicTacToeAchievementAchievedEvent");
        }

        return new UnifiedAchievementAchievedEvent(
                tttEvent.playerId(),
                tttEvent.achievementId(),
                tttEvent.achievementType(),
                tttEvent.matchId(),
                "TICTACTOE",
                tttEvent.messageType(),
                tttEvent.timestamp()
        );
    }

    @Override
    public boolean canTranslate(DomainEvent event) {
        return event instanceof TicTacToeMatchCreatedEvent ||
                event instanceof TicTacToeMatchMoveMadeEvent ||
                event instanceof TicTacToeMatchEndedEvent ||
                event instanceof TicTacToeAchievementAchievedEvent;
    }
}