package be.kdg.gameplay.port.in.command;

import java.util.UUID;

public record RegisterGameViewProjectionCommand(
        UUID gameId,
        String name
) {}
