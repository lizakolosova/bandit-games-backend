package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.events.GameRoomInvitationSentEvent;
import be.kdg.gameplay.port.in.command.InvitationNotificationCommand;
import be.kdg.gameplay.port.in.InvitationNotificationEventProjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameRoomInvitationEventListener {

    private final InvitationNotificationEventProjector notifier;
    private static final Logger logger = LoggerFactory.getLogger(GameRoomInvitationEventListener.class);


    public GameRoomInvitationEventListener(InvitationNotificationEventProjector notifier) {
        this.notifier = notifier;
    }

    @EventListener(GameRoomInvitationSentEvent.class)
    public void on(GameRoomInvitationSentEvent event) {
        logger.info("Game room invitation sent event received: {}", event);
        InvitationNotificationCommand command = new InvitationNotificationCommand(
                event.invitedPlayerId(),
                event.gameRoomId(),
                event.hostPlayerId()
        );

        notifier.project(command);
    }
}
