package be.kdg.player.port.in;

import java.util.UUID;

public record LoadLibraryGameCommand(UUID playerId, UUID gameId) { }