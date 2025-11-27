package be.kdg.gameplay.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.exception.GameRoomException;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.domain.valueobj.MatchId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameRoom {

    private GameRoomId gameRoomId;
    private GameId gameId;

    private PlayerId hostPlayerId;
    private PlayerId invitedPlayerId;

    private GameRoomType gameRoomType;
    private GameRoomStatus status;

    private final LocalDateTime createdAt;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

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
        this(LocalDateTime.now(), GameRoomId.create(), gameId,  hostPlayerId, invitedPlayerId, gameRoomType, GameRoomStatus.READY);
    }


    public void join(PlayerId player) {
        if (isFull())
            throw GameRoomException.invite();

        if (gameRoomType == GameRoomType.CLOSED && !player.equals(invitedPlayerId))
            throw GameRoomException.notReady();

        this.invitedPlayerId = player;
    }

    private boolean isFull() {
        return invitedPlayerId != null;
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

    public List<DomainEvent> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public GameRoomId getGameRoomId() {
        return gameRoomId;
    }

    public GameId getGameId() {
        return gameId;
    }

    public PlayerId getHostPlayerId() {
        return hostPlayerId;
    }

    public PlayerId getInvitedPlayerId() {
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

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
}