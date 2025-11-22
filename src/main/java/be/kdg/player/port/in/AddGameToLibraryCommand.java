package be.kdg.player.port.in;

import java.util.UUID;

public record AddGameToLibraryCommand(UUID playerId, UUID gameId) { }

