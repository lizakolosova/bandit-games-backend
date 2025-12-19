package be.kdg.player.port.in.command;

public record AchievementAddedProjectionCommand(
        String achievementId,
        String gameId,
        String name,
        String description
) {}
