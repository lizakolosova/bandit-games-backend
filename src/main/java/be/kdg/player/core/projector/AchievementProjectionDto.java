package be.kdg.player.core.projector;

import be.kdg.common.valueobj.GameId;

import java.util.UUID;

public record AchievementProjectionDto(UUID achievementId, UUID gameId) {}