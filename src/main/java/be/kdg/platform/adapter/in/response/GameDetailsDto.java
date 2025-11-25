package be.kdg.platform.adapter.in.response;

import be.kdg.platform.domain.Game;

import java.time.LocalDate;
import java.util.List;

public record GameDetailsDto(
        String id,
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate releaseDate,
        Integer averageMinutes,
        List<AchievementDto> achievements
) {
    public static GameDetailsDto toDetailsDto(Game game) {

        List<AchievementDto> achievementDtos =
                game.getAchievements().stream()
                        .map(a -> new AchievementDto(
                                a.getAchievementId().uuid().toString(),
                                a.getName(),
                                a.getDescription(),
                                a.getHowToUnlock()
                        ))
                        .toList();

        return new GameDetailsDto(
                game.getGameId().uuid().toString(),
                game.getName(),
                game.getRules(),
                game.getPictureUrl(),
                game.getGameUrl(),
                game.getCategory(),
                game.getDevelopedBy(),
                game.getCreatedAt(),
                game.getAverageMinutes(),
                achievementDtos
        );
    }
}