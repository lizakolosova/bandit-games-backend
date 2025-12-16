package be.kdg.player.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.FriendshipRequestAcceptedEvent;
import be.kdg.common.exception.InvalidRowException;
import be.kdg.player.domain.valueobj.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRequest {

    private FriendshipRequestId requestId;
    private SenderId senderId;
    private ReceiverId receiverId;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public FriendshipRequest(FriendshipRequestId requestId, SenderId senderId, ReceiverId receiverId, FriendshipStatus status, LocalDateTime createdAt) {
        this.receiverId = receiverId;
        this.requestId = requestId;
        this.senderId = senderId;
        this.status = status;
        this.createdAt = createdAt;
    }


    public FriendshipRequest(SenderId senderId, ReceiverId receiverId) {
        this(FriendshipRequestId.create(), senderId, receiverId, FriendshipStatus.PENDING, LocalDateTime.now());
    }

    public void accept() {
        if (status != FriendshipStatus.PENDING)
            throw new InvalidRowException("Request already processed.");
        status = FriendshipStatus.ACCEPTED;
        registerEvent((new FriendshipRequestAcceptedEvent(requestId.uuid(), senderId.uuid(), receiverId.uuid())));
    }

    public void reject() {
        if (status != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Only pending friend requests can be rejected.");
        }
        status = FriendshipStatus.REJECTED;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public FriendshipRequestId getFriendshipRequestId() {
        return requestId;
    }

    public SenderId getSenderId() {
        return senderId;
    }

    public ReceiverId getReceiverId() {
        return receiverId;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}