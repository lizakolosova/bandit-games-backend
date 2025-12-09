package be.kdg.player.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.GameAddedEvent;
import be.kdg.common.events.chess.ChessGameRegisteredEvent;
import be.kdg.player.port.in.command.GameAddedProjectionCommand;
import be.kdg.player.port.in.GameProjector;
import be.kdg.player.port.in.command.RegisterPlayerGameProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventListener {

    private static final Logger logger = LoggerFactory.getLogger(GameEventListener.class);

    private final GameProjector projector;

    public GameEventListener(GameProjector projector) {
        this.projector = projector;
    }

    @EventListener(GameAddedEvent.class)
    public void onGameAdded(GameAddedEvent event) {
        logger.info("Game added event received: {}", event);

        projector.project(new GameAddedProjectionCommand(
                event.gameId(),
                event.name(),
                event.rules(),
                event.pictureUrl(),
                event.category(),
                event.developedBy(),
                event.createdAt(),
                event.averageMinutes(),
                event.achievementCount()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.PLAYER_GAME_REGISTERED_QUEUE)
    public void onCatalogUpdate(ChessGameRegisteredEvent event) {
        logger.info("Game registered event received: {}", event);
        projector.project(new RegisterPlayerGameProjectionCommand(
                event.registrationId(),
                "CHESS",
                event.pictureUrl(),
                "Board game",
                "Standard rules",
                event.availableAchievements().size(),
                15,
                "Chess platform"
        ));
    }
}

