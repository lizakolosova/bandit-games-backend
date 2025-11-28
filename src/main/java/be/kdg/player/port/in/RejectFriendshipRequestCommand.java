package be.kdg.player.port.in;

import be.kdg.player.domain.valueobj.FriendshipRequestId;

import java.util.UUID;

public record RejectFriendshipRequestCommand(
        UUID friendshipRequestId,
        UUID receiverId
) {}

