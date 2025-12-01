package be.kdg.gameplay.port.in;

public interface InvitationNotificationEventProjector {
    void project(InvitationNotificationCommand command);
}

