package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.domain.valueobj.InvitationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_room", schema = "kdg_gameplay")
public class GameRoomJpaEntity {

    @Id
    private UUID uuid;

    private UUID gameId;

    private String hostPlayerName;

    private String invitedPlayerName;

    private UUID hostPlayerId;

    private UUID invitedPlayerId;

    @Enumerated(EnumType.STRING)
    private GameRoomType gameRoomType;

    @Enumerated(EnumType.STRING)
    private GameRoomStatus status;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;

    protected GameRoomJpaEntity() {}

    public GameRoomJpaEntity(UUID uuid, UUID gameId, String hostPlayerName, String invitedPlayerName, UUID hostPlayerId,
                             UUID invitedPlayerId, GameRoomType gameRoomType, GameRoomStatus status, LocalDateTime createdAt,
                             InvitationStatus invitationStatus) {
        this.uuid = uuid;
        this.gameId = gameId;
        this.hostPlayerName = hostPlayerName;
        this.invitedPlayerName = invitedPlayerName;
        this.hostPlayerId = hostPlayerId;
        this.invitedPlayerId = invitedPlayerId;
        this.gameRoomType = gameRoomType;
        this.status = status;
        this.createdAt = createdAt;
        this.invitationStatus = invitationStatus;
    }

    public String getHostPlayerName() {
        return hostPlayerName;
    }

    public String getInvitedPlayerName() {
        return invitedPlayerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getHostPlayerId() {
        return hostPlayerId;
    }

    public UUID getInvitedPlayerId() {
        return invitedPlayerId;
    }

    public GameRoomType getGameRoomType() {
        return gameRoomType;
    }

    public GameRoomStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }
}

