package be.kdg.platform.port.in.command;

public record AchievementDefinitionCommand(
        String name,
        String description,
        String howToUnlock
) {}