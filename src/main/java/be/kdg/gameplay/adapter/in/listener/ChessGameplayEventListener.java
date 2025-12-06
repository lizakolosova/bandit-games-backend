package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.chess.ChessGameRegisteredEvent;
import be.kdg.common.events.chess.ChessMatchCreatedEvent;
import be.kdg.common.events.chess.ChessMatchEndedEvent;
import be.kdg.common.events.chess.ChessMatchUpdatedEvent;
import be.kdg.gameplay.port.in.ChessGameProjector;
import be.kdg.gameplay.port.in.command.ChessGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameUpdatedProjectionCommand;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
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
    public void onGameCreated(ChessMatchCreatedEvent event) {
        logger.info("Match is created: {}", event);
        projector.project(new ChessGameCreatedProjectionCommand(
                event.gameId(),
                "Chess",
                event.whitePlayerId(),
                event.blackPlayerId(),
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_UPDATED_QUEUE)
    public void onGameUpdated(ChessMatchUpdatedEvent event) {
        logger.info("Match is updated: {}", event);
        projector.project(new ChessGameUpdatedProjectionCommand(
                event.gameId(),
                event.whitePlayerId(),
                event.blackPlayerId(),
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_ENDED_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void onGameEnded(ChessMatchEndedEvent event) {
        logger.info("Chess game ended: {} - Winner: {}", event.gameId(), event.winner());
        projector.project(new ChessGameEndedProjectionCommand(
                event.gameId(),
                event.whitePlayerId(),
                event.blackPlayerId(),
                event.winner(),
                event.endReason(),
                event.totalMoves(),
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.GAMEPLAY_GAME_REGISTERED_QUEUE)
    public void onGameRegistered(ChessGameRegisteredEvent event) {
        projector.project(new RegisterGameViewProjectionCommand(
                event.registrationId(),
                "Chess"
        ));
    }
}