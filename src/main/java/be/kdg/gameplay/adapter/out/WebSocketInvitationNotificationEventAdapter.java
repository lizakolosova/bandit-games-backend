package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.port.in.command.InvitationNotificationCommand;
import be.kdg.gameplay.port.in.InvitationNotificationEventProjector;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInvitationNotificationEventAdapter implements InvitationNotificationEventProjector {

    private final SimpMessagingTemplate messaging;

    public WebSocketInvitationNotificationEventAdapter(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @Override
    public void project(InvitationNotificationCommand command) {
        messaging.convertAndSend(
                "/topic/invitations/" + command.recipientId(),
                command
        );
    }
}
