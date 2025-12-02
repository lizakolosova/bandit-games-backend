package be.kdg.player.adapter.in;

import be.kdg.common.events.FriendshipRequestAcceptedEvent;
import be.kdg.player.port.in.command.FriendshipRequestAcceptedProjectionCommand;
import be.kdg.player.port.in.FriendshipRequestAcceptedProjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FriendshipRequestEventListener {

    private static final Logger logger = LoggerFactory.getLogger(FriendshipRequestEventListener.class);

    private final FriendshipRequestAcceptedProjector projector;

    public FriendshipRequestEventListener(FriendshipRequestAcceptedProjector projector) {
        this.projector = projector;
    }

    @EventListener(FriendshipRequestAcceptedEvent.class)
    public void onFriendshipRequestAccepted(FriendshipRequestAcceptedEvent event) {
        logger.info("Friendship request accepted event received: {}", event);

        projector.project(new FriendshipRequestAcceptedProjectionCommand(
                event.friendshipRequestId(),
                event.senderId(),
                event.receiverId()
        ));
    }
}

