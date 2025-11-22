package be.kdg.gameplay.port.in;

import java.util.UUID;

public record CreateSoloMatchCommand(
        UUID playerId,
        UUID gameId
) {}
