package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.port.in.command.RejectFriendshipRequestCommand;

public interface RejectFriendshipRequestUseCase {
    FriendshipRequest reject(RejectFriendshipRequestCommand command);
}

