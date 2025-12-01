package be.kdg.player.port.in;

import java.util.UUID;

public record FriendRemovedProjectionCommand(UUID playerId, UUID friendId) { }
