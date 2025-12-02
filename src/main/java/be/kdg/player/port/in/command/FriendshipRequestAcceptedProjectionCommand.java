package be.kdg.player.port.in.command;

import java.util.UUID;

public record FriendshipRequestAcceptedProjectionCommand(UUID friendshipId, UUID senderId, UUID receiverId) {}