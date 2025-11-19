package be.kdg.gameplay;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.valueobj.GameRoomId;
import be.kdg.gameplay.valueobj.GameRoomStatus;
import be.kdg.gameplay.valueobj.LobbyType;
import be.kdg.gameplay.valueobj.MatchId;
import be.kdg.player.domain.valueobj.PlayerAchievementId;

import java.time.LocalDateTime;
import java.util.List;

public class GameRoom {

    private GameRoomId gameRoomId;
    private GameId gameId;

    private PlayerId hostPlayerId;
    private PlayerId invitedPlayerId;

    private LobbyType lobbyType;
    private GameRoomStatus status;

    private final LocalDateTime createdAt;

    public GameRoom(LocalDateTime createdAt, GameRoomId gameRoomId, GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, LobbyType lobbyType, GameRoomStatus status) {
        this.createdAt = createdAt;
        this.gameRoomId = gameRoomId;
        this.gameId = gameId;
        this.hostPlayerId = hostPlayerId;
        this.invitedPlayerId = invitedPlayerId;
        this.lobbyType = lobbyType;
        this.status = status;
    }

    public GameRoom(GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, LobbyType lobbyType) {
        this(LocalDateTime.now(), GameRoomId.create(), gameId,  hostPlayerId, invitedPlayerId, lobbyType, GameRoomStatus.WAITING);
    }

    public void invite(PlayerId invited) {
        this.invitedPlayerId = invited;
        updateReadyStatus();
    }

    public void join(PlayerId player) {
        if (isFull())
            throw new IllegalStateException("Lobby already has two players.");

        if (lobbyType == LobbyType.CLOSED && !player.equals(invitedPlayerId))
            throw new IllegalStateException("This lobby is invite-only");

        this.invitedPlayerId = player;
        updateReadyStatus();
    }

    private boolean isFull() {
        return invitedPlayerId != null;
    }

    private void updateReadyStatus() {
        if (isFull()) {
            status = GameRoomStatus.READY;
        }
    }

    public void expire() {
        this.status = GameRoomStatus.EXPIRED;
    }

    public Match startMatch() {
        if (status != GameRoomStatus.READY)
            throw new IllegalStateException("Lobby not ready");

        status = GameRoomStatus.MATCH_STARTED;

        return new Match(
                MatchId.create(),
                gameId,
                List.of(hostPlayerId, invitedPlayerId)
        );
    }
}

