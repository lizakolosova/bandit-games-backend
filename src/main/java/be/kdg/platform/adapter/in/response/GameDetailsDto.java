package be.kdg.platform.adapter.in.response;

import be.kdg.platform.domain.Game;

import java.time.LocalDate;

public record GameDetailsDto(
        String id,
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate releaseDate,
        Integer averageMinutes
) {
    public static GameDetailsDto toDetailsDto(Game game) {
        return new GameDetailsDto(
                game.getGameId().uuid().toString(),
                game.getName(),
                game.getRules(),
                game.getPictureUrl(),
                game.getGameUrl(),
                game.getCategory(),
                game.getDevelopedBy(),
                game.getCreatedAt(),
                game.getAverageMinutes()
        );
    }

}
