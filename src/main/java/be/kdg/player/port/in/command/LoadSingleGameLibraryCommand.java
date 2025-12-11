package be.kdg.player.port.in.command;

import java.util.UUID;

public record LoadSingleGameLibraryCommand(UUID playerId, UUID gameId) {}
