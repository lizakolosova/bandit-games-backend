package be.kdg.player.port.in;

import be.kdg.player.domain.valueobj.FriendshipRequestId;

import java.util.UUID;

public record RejectFriendshipRequestCommand(
        FriendshipRequestId friendshipRequestId,
        UUID receiverId
) {}

