package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.MatchCreatedEvent;
import be.kdg.common.events.MatchEndedEvent;
import be.kdg.common.events.MatchUpdatedEvent;
import be.kdg.gameplay.port.in.ChessGameProjector;
import be.kdg.gameplay.port.in.command.ChessGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameUpdatedProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ChessGameplayEventListener {

    private final ChessGameProjector projector;

    private static final Logger logger = LoggerFactory.getLogger(ChessGameplayEventListener.class);

    public ChessGameplayEventListener(ChessGameProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_CREATED_QUEUE)
    public void onGameCreated(MatchCreatedEvent event) {
        logger.info("Match is created: {}", event);
        projector.project(new ChessGameCreatedProjectionCommand(
                event.gameId(),
                "Chess",
                "918dfd56-094b-4f5f-a356-06670fcbf7d5",
                "a275b4b9-0218-45a9-a9e6-844ad7c9e238",
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_UPDATED_QUEUE)
    public void onGameUpdated(MatchUpdatedEvent event) {
        logger.info("Match is updated: {}", event);
        projector.project(new ChessGameUpdatedProjectionCommand(
                event.gameId(),
                "918dfd56-094b-4f5f-a356-06670fcbf7d5",
                "a275b4b9-0218-45a9-a9e6-844ad7c9e238",
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_ENDED_QUEUE)
    public void onGameEnded(MatchEndedEvent event) {
        logger.info("Chess game ended: {} - Winner: {}", event.gameId(), event.winner());
        projector.project(new ChessGameEndedProjectionCommand(
                event.gameId(),
                "918dfd56-094b-4f5f-a356-06670fcbf7d5",
                "a275b4b9-0218-45a9-a9e6-844ad7c9e238",
                event.winner(),
                event.endReason(),
                event.totalMoves(),
                event.timestamp()
        ));
    }
}