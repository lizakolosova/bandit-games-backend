package be.kdg.gameplay.port.in.command;

public record RegisterGameViewProjectionCommand(
        String gameId,
        String name
) {}
