package be.kdg.player.port.in.command;

import java.util.UUID;

public record AddGameToLibraryCommand(UUID playerId, UUID gameId) {}

