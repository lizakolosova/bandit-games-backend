package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.chess.ChessGameRegisteredEvent;
import be.kdg.gameplay.port.in.RegisterGameProjector;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GameRegisteredEventListener {

    private final RegisterGameProjector projector;

    public GameRegisteredEventListener(RegisterGameProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.GAMEPLAY_GAME_REGISTERED_QUEUE)
    public void onGameRegistered(ChessGameRegisteredEvent event) {
        projector.project(new RegisterGameViewProjectionCommand(
                event.registrationId(),
                "Chess"
        ));
    }
}
