package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.events.GameApprovedEvent;
import be.kdg.gameplay.port.in.RegisterGameProjector;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameApprovedEventListener {

    private final RegisterGameProjector projector;

    public GameApprovedEventListener(RegisterGameProjector projector) {
        this.projector = projector;
    }

    @EventListener(GameApprovedEvent.class)
    public void onGameRegistered(GameApprovedEvent event) {
        projector.project(new RegisterGameViewProjectionCommand(
                event.gameId(),
                event.name()
        ));
    }
}
