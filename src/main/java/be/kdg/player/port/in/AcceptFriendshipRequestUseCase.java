package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.port.in.command.AcceptFriendshipRequestCommand;

public interface AcceptFriendshipRequestUseCase {
    FriendshipRequest accept(AcceptFriendshipRequestCommand command);
}

