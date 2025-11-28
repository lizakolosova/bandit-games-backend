package be.kdg.player.adapter.out;

import be.kdg.player.domain.valueobj.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendshipRequestJpaRepository extends JpaRepository<FriendshipRequestJpaEntity, UUID> {
    boolean existsBySenderIdAndReceiverIdAndStatus(UUID senderId, UUID receiverId, FriendshipStatus status);
}