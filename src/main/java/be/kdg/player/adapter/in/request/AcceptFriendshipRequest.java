package be.kdg.player.adapter.in.request;

import java.util.UUID;

public record AcceptFriendshipRequest(
        UUID friendshipRequestId
) {}
