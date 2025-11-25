package be.kdg.platform.port.in;

public record AchievementDefinitionCommand(
        String name,
        String description,
        String howToUnlock
) {}