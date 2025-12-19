package be.kdg.player.port.in.command;

import be.kdg.player.domain.FriendshipRequest;

public record PendingFriendshipRequestView(
        FriendshipRequest request,
        String senderUsername,
        String senderPictureUrl
) {}

