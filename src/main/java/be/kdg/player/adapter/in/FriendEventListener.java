package be.kdg.player.adapter.in;

import be.kdg.common.events.FriendRemovedEvent;
import be.kdg.player.port.in.command.FriendRemovedProjectionCommand;
import be.kdg.player.port.in.FriendRemovedProjector;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FriendEventListener {

    private final FriendRemovedProjector projector;

    public FriendEventListener(FriendRemovedProjector projector) {
        this.projector = projector;
    }

    @EventListener
    public void onFriendRemoved(FriendRemovedEvent event) {
        projector.project(
                new FriendRemovedProjectionCommand(
                        event.playerId(),
                        event.friendId()
                )
        );
    }
}

