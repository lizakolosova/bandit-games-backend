package be.kdg.player.port.in.command;

import java.util.UUID;

public record MarkFavouriteCommand(UUID playerId, UUID gameId, boolean favourite) { }

