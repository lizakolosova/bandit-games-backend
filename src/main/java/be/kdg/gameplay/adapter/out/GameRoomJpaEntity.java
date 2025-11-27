package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_room", schema = "kdg_gameplay")
public class GameRoomJpaEntity {

    @Id
    private UUID uuid;

    private UUID gameId;

    private UUID hostPlayerId;

    private UUID invitedPlayerId;

    @Enumerated(EnumType.STRING)
    private GameRoomType gameRoomType;

    @Enumerated(EnumType.STRING)
    private GameRoomStatus status;

    private LocalDateTime createdAt;

    protected GameRoomJpaEntity() {}

    public GameRoomJpaEntity(
            UUID uuid,
            UUID gameId,
            UUID hostPlayerId,
            UUID invitedPlayerId,
            GameRoomType type,
            GameRoomStatus status,
            LocalDateTime createdAt
    ) {
        this.uuid = uuid;
        this.gameId = gameId;
        this.hostPlayerId = hostPlayerId;
        this.invitedPlayerId = invitedPlayerId;
        this.gameRoomType = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getUuid() { return uuid; }
    public UUID getGameId() { return gameId; }
    public UUID getHostPlayerId() { return hostPlayerId; }
    public UUID getInvitedPlayerId() { return invitedPlayerId; }
    public GameRoomType getGameRoomType() { return gameRoomType; }
    public GameRoomStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

