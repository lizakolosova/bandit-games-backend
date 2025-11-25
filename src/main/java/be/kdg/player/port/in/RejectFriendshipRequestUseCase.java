package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;

public interface RejectFriendshipRequestUseCase {
    FriendshipRequest reject(RejectFriendshipRequestCommand command);
}

