package be.kdg.gameplay.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.GameRoomInvitationSentEvent;
import be.kdg.common.events.MatchBeforeStartedEvent;
import be.kdg.common.exception.GameRoomException;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.valueobj.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoom {

    private GameRoomId gameRoomId;
    private GameId gameId;
    private String hostplayerName;
    private String invitedPlayerName;


    private PlayerId hostPlayerId;
    private PlayerId invitedPlayerId;

    private GameRoomType gameRoomType;
    private GameRoomStatus status;
    private InvitationStatus invitationStatus;

    private final LocalDateTime createdAt;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public GameRoom(LocalDateTime createdAt, GameRoomId gameRoomId, GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, GameRoomType gameRoomType, GameRoomStatus status, InvitationStatus invitationStatus) {
        this.createdAt = createdAt;
        this.gameRoomId = gameRoomId;
        this.gameId = gameId;
        this.hostPlayerId = hostPlayerId;
        this.invitedPlayerId = invitedPlayerId;
        this.gameRoomType = gameRoomType;
        this.status = status;
        this.invitationStatus = invitationStatus;
    }

    public GameRoom(GameId gameId, PlayerId hostPlayerId, PlayerId invitedPlayerId, GameRoomType gameRoomType) {
        this(LocalDateTime.now(), GameRoomId.create(), gameId,  hostPlayerId, invitedPlayerId, gameRoomType,
                GameRoomStatus.WAITING, InvitationStatus.PENDING);
        if (invitedPlayerId != null) {
            registerEvent(new GameRoomInvitationSentEvent(gameRoomId.uuid(), hostPlayerId.uuid(), invitedPlayerId.uuid()));
        }
    }

    public void acceptInvitation(PlayerId player) {
        if (!player.equals(invitedPlayerId))
            throw GameRoomException.notAllowed();

        if (invitationStatus != InvitationStatus.PENDING)
            throw GameRoomException.notReady();

        this.invitationStatus = InvitationStatus.ACCEPTED;
        this.status = GameRoomStatus.READY;
    }

    public void rejectInvitation(PlayerId player) {
        if (!player.equals(invitedPlayerId))
            throw GameRoomException.notAllowed();

        if (invitationStatus != InvitationStatus.PENDING)
            throw GameRoomException.notReady();

        this.invitationStatus = InvitationStatus.REJECTED;
        this.status = GameRoomStatus.WAITING;
        this.invitedPlayerId = null;
    }
    public Match beforeStartMatch() {
        if (status != GameRoomStatus.READY)
            throw GameRoomException.notReady();

        status = GameRoomStatus.MATCH_STARTED;
        Match match = new Match(hostplayerName, invitedPlayerName, List.of(hostPlayerId, invitedPlayerId));
        registerEvent(new MatchBeforeStartedEvent(hostplayerName,
                invitedPlayerName, hostPlayerId.uuid(), invitedPlayerId.uuid()));
        return match;
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

    public void setStatus(GameRoomStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }
}