package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;

public interface SendFriendshipRequestUseCase {
    FriendshipRequest sendRequest(SendFriendshipRequestCommand command);
}
