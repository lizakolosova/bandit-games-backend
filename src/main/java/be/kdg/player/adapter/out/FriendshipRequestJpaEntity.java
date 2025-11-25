package be.kdg.player.adapter.out;

import be.kdg.player.domain.valueobj.FriendshipStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friendship_request", schema = "kdg_player")
public class FriendshipRequestJpaEntity {

    @Id
    private UUID uuid;

    private UUID senderId;

    private UUID receiverId;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    private LocalDateTime createdAt;

    protected FriendshipRequestJpaEntity() {}

    public FriendshipRequestJpaEntity(UUID uuid, UUID senderId, UUID receiverId, FriendshipStatus status, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getUuid() { return uuid; }
    public FriendshipStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }
}