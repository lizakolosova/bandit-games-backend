package be.kdg.player.port.in;

import be.kdg.player.domain.FriendshipRequest;

import java.util.List;
import java.util.UUID;

public interface PendingFriendshipRequestUseCase {
    List<FriendshipRequest> getPendingRequests(UUID receiverId);
}
