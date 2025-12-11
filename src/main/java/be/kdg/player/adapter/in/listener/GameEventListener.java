package be.kdg.player.adapter.in.listener;

import be.kdg.common.events.GameApprovedEvent;
import be.kdg.player.port.in.command.GameAddedProjectionCommand;
import be.kdg.player.port.in.GameProjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventListener {

    private static final Logger logger = LoggerFactory.getLogger(GameEventListener.class);

    private final GameProjector projector;

    public GameEventListener(GameProjector projector) {
        this.projector = projector;
    }

    @EventListener(GameApprovedEvent.class)
    public void onGameAdded(GameApprovedEvent event) {
        logger.info("Game approved event received: {}", event);

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
}

