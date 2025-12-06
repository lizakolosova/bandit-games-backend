package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.port.in.command.SendFriendshipRequestCommand;

public interface SendFriendshipRequestUseCase {
    FriendshipRequest sendRequest(SendFriendshipRequestCommand command);
}
