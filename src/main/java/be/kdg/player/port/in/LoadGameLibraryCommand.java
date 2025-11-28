package be.kdg.player.port.in;

import java.util.UUID;

public record LoadGameLibraryCommand(UUID playerId) {}