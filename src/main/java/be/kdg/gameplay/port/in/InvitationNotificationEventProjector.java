package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.InvitationNotificationCommand;

public interface InvitationNotificationEventProjector {
    void project(InvitationNotificationCommand command);
}

