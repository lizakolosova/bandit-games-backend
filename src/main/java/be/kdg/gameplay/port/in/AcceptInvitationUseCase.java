package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.AcceptInvitationCommand;

public interface AcceptInvitationUseCase {
    void accept(AcceptInvitationCommand command);
}