package be.kdg.player.domain;

import be.kdg.common.exception.InvalidRowException;
import be.kdg.player.domain.valueobj.*;

import java.time.LocalDateTime;

public class FriendshipRequest {

    private FriendshipRequestId requestId;
    private SenderId senderId;
    private ReceiverId receiverId;
    private FriendshipStatus status;
    private LocalDateTime createdAt;

    public FriendshipRequest(FriendshipRequestId requestId, SenderId senderId, ReceiverId receiverId, FriendshipStatus status, LocalDateTime createdAt) {
        this.receiverId = receiverId;
        this.requestId = requestId;
        this.senderId = senderId;
        this.status = status;
        this.createdAt = createdAt;
    }


    public FriendshipRequest(SenderId senderId, ReceiverId receiverId) {
        this(FriendshipRequestId.create(), senderId, receiverId, FriendshipStatus.PENDING, LocalDateTime.now());
        // we'll have an event here
    }

    public void accept() {
        if (status != FriendshipStatus.PENDING)
            throw new InvalidRowException("Request already processed.");
        status = FriendshipStatus.ACCEPTED;
        // we'll have an event here
    }

    public void reject() {
        if (status != FriendshipStatus.PENDING)
            throw new InvalidRowException("Request already processed.");
        status = FriendshipStatus.REJECTED;
        // we'll have an event here
    }

    public void expire() {
        if (status == FriendshipStatus.PENDING)
            status = FriendshipStatus.EXPIRED;
        // we'll have an event here
    }

    public FriendshipRequestId getRequestId() {
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

