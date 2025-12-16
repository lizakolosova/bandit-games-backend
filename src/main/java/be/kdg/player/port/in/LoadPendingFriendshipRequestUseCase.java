package be.kdg.player.port.in;

import be.kdg.player.port.in.command.PendingFriendshipRequestView;

import java.util.List;
import java.util.UUID;

public interface LoadPendingFriendshipRequestUseCase {
    List<PendingFriendshipRequestView> getPendingRequests(UUID receiverId);
}
