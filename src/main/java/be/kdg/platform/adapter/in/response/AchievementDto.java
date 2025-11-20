package be.kdg.platform.adapter.in.response;

public record AchievementDto(
        String id,
        String name,
        String description,
        String howToUnlock
) {}
