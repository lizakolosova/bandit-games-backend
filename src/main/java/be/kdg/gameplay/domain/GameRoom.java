package be.kdg.gameplay.domain;

import be.kdg.common.exception.GameRoomException;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.domain.valueobj.MatchId;

import java.time.LocalDateTime;
import java.util.List;

public class GameRoom {

    private GameRoomId gameRoomId;
    private GameId gameId;

    private PlayerId hostPlayerId;
    private PlayerId invitedPlayerId;

    private GameRoomType gameRoomType;
    private GameRoomStatus status;

    private final LocalDateTime createdAt;

    public GameRoom(LocalDateTime createdAt, GameRoomId gameRoomId, GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, GameRoomType gameRoomType, GameRoomStatus status) {
        this.createdAt = createdAt;
        this.gameRoomId = gameRoomId;
        this.gameId = gameId;
        this.hostPlayerId = hostPlayerId;
        this.invitedPlayerId = invitedPlayerId;
        this.gameRoomType = gameRoomType;
        this.status = status;
    }

    public GameRoom(GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, GameRoomType gameRoomType) {
        this(LocalDateTime.now(), GameRoomId.create(), gameId,  hostPlayerId, invitedPlayerId, gameRoomType, GameRoomStatus.WAITING);
    }

    public void invite(PlayerId invited) {
        this.invitedPlayerId = invited;
        updateReadyStatus();
    }

    public void join(PlayerId player) {
        if (isFull())
            throw GameRoomException.invite();

        if (gameRoomType == GameRoomType.CLOSED && !player.equals(invitedPlayerId))
            throw GameRoomException.notReady();

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
            throw GameRoomException.notReady();

        status = GameRoomStatus.MATCH_STARTED;

        return new Match(
                MatchId.create(),
                gameId,
                List.of(hostPlayerId, invitedPlayerId)
        );
    }
}

