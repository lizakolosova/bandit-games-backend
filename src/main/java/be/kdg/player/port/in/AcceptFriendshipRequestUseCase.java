package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;

public interface AcceptFriendshipRequestUseCase {
    FriendshipRequest accept(AcceptFriendshipRequestCommand command);
}

