package be.kdg.player.adapter.out;

import be.kdg.player.domain.valueobj.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendshipRequestJpaRepository extends JpaRepository<FriendshipRequestJpaEntity, UUID> {
    boolean existsBySenderIdAndReceiverIdAndStatus(UUID senderId, UUID receiverId, FriendshipStatus status);
}