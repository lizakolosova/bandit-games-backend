package be.kdg.platform.adapter.in;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.AchievementEntry;
import be.kdg.common.events.GameRegisteredEvent;
import be.kdg.platform.port.in.ChessPlatformProjector;
import be.kdg.platform.port.in.command.RegisterChessGameProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ChessPlatformEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChessPlatformEventListener.class);

    private final ChessPlatformProjector projector;

    public ChessPlatformEventListener(ChessPlatformProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.PLATFORM_GAME_REGISTERED_QUEUE)
    public void onGameRegistered(GameRegisteredEvent event) {
        log.info("Received chess game registration: {}", event.registrationId());

        var achievements = event.availableAchievements().stream()
                .map(a -> new AchievementEntry(
                        a.code(),
                        a.description()
                ))
                .toList();

        projector.project(new RegisterChessGameProjectionCommand(
                event.registrationId(),
                event.frontendUrl(),
                event.pictureUrl(),
                achievements,
                event.timestamp()
        ));
    }
}