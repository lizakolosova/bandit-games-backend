package be.kdg.player.port.in;

import java.util.UUID;

public record RemoveFriendCommand(UUID playerId, UUID friendId) { }

