package be.kdg.player.port.out;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;

import java.util.List;
import java.util.UUID;

public interface LoadFriendshipRequestPort {
    boolean existsPending(SenderId senderId, ReceiverId receiverId);
    FriendshipRequest load(FriendshipRequestId friendshipRequestId);
    List<FriendshipRequest> findPendingByReceiverId(ReceiverId receiverId);

}
