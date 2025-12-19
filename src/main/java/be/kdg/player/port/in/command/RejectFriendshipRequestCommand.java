package be.kdg.player.port.in.command;

import java.util.UUID;

public record RejectFriendshipRequestCommand(
        UUID friendshipRequestId,
        UUID receiverId
) {}

