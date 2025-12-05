package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.GameRegisteredEvent;
import be.kdg.gameplay.port.in.GameplayGameProjector;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GameplayGameRegisteredListener {

    private final GameplayGameProjector projector;

    public GameplayGameRegisteredListener(GameplayGameProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.GAMEPLAY_GAME_REGISTERED_QUEUE)
    public void onGameRegistered(GameRegisteredEvent event) {
        projector.project(new RegisterGameViewProjectionCommand(
                event.registrationId(),
                "Chess"
        ));
    }
}

