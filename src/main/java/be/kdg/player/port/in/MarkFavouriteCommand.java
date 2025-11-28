package be.kdg.player.port.in;

import java.util.UUID;

public record MarkFavouriteCommand(UUID playerId, UUID gameId, boolean favourite) { }

