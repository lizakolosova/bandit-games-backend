package be.kdg.player.adapter.out;

import be.kdg.common.exception.NotFoundException;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.AddFriendshipRequestPort;
import be.kdg.player.domain.valueobj.FriendshipStatus;
import be.kdg.player.port.out.UpdateFriendshipRequestPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FriendshipRequestJpaAdapter implements AddFriendshipRequestPort, LoadFriendshipRequestPort, UpdateFriendshipRequestPort {

    private final FriendshipRequestJpaRepository requests;

    public FriendshipRequestJpaAdapter(FriendshipRequestJpaRepository requests) {
        this.requests = requests;
    }

    @Override
    public void save(FriendshipRequest request) {
        FriendshipRequestJpaEntity entity =
                new FriendshipRequestJpaEntity(request.getFriendshipRequestId().uuid(), request.getSenderId().uuid(),
                        request.getReceiverId().uuid(), request.getStatus(), request.getCreatedAt());

        requests.save(entity);
    }

    @Override
    public boolean existsPending(SenderId senderId, ReceiverId receiverId) {
        return requests.existsBySenderIdAndReceiverIdAndStatus(senderId.uuid(), receiverId.uuid(), FriendshipStatus.PENDING);
    }

    @Override
    public FriendshipRequest load(FriendshipRequestId friendshipRequestId) {
        return requests.findById(friendshipRequestId.uuid())
                .map(request -> new FriendshipRequest(FriendshipRequestId.of(request.getUuid()),
                        SenderId.of(request.getSenderId()), ReceiverId.of(request.getReceiverId()), request.getStatus(),
                        request.getCreatedAt()))
                .orElseThrow(() -> new NotFoundException("Friendship not found"));
    }

    @Override
    public List<FriendshipRequest> findPendingByReceiverId(ReceiverId receiverId) {
        return requests.findByReceiverIdAndStatus(receiverId.uuid(), FriendshipStatus.PENDING)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public FriendshipRequest update(FriendshipRequest request) {
        FriendshipRequestJpaEntity saved = requests.save(new FriendshipRequestJpaEntity(request.getFriendshipRequestId().uuid(),
                request.getSenderId().uuid(), request.getReceiverId().uuid(), request.getStatus(), request.getCreatedAt()));
        return new FriendshipRequest(FriendshipRequestId.of(saved.getUuid()),
                SenderId.of(saved.getSenderId()), ReceiverId.of(saved.getReceiverId()), saved.getStatus(),
                saved.getCreatedAt());
    }

    private FriendshipRequest toDomain(FriendshipRequestJpaEntity entity) {
        return new FriendshipRequest(
                FriendshipRequestId.of(entity.getUuid()),
                SenderId.of(entity.getSenderId()),
                ReceiverId.of(entity.getReceiverId()),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

}

