package be.kdg.player.adapter.out;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.SaveFriendshipRequestPort;
import be.kdg.player.domain.valueobj.FriendshipStatus;
import org.springframework.stereotype.Repository;

@Repository
public class FriendshipRequestJpaAdapter implements SaveFriendshipRequestPort, LoadFriendshipRequestPort {

    private final FriendshipRequestJpaRepository requests;

    public FriendshipRequestJpaAdapter(FriendshipRequestJpaRepository requests) {
        this.requests = requests;
    }

    @Override
    public void save(FriendshipRequest request) {
        FriendshipRequestJpaEntity entity =
                new FriendshipRequestJpaEntity(
                        request.getRequestId().uuid(),
                        request.getSenderId().uuid(),
                        request.getReceiverId().uuid(),
                        request.getStatus(),
                        request.getCreatedAt()
                );

        requests.save(entity);
    }

    @Override
    public boolean existsPending(SenderId senderId, ReceiverId receiverId) {
        return requests.existsBySenderIdAndReceiverIdAndStatus(
                senderId.uuid(), receiverId.uuid(), FriendshipStatus.PENDING
        );
    }
}

