package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.RejectInvitationCommand;

public interface RejectInvitationUseCase {
    void reject(RejectInvitationCommand command);
}