package be.kdg.platform.adapter.in.response;

import be.kdg.platform.domain.Game;

public record GameDto(
        String id,
        String name,
        String rules,
        String pictureUrl,
        String category,
        String gameUrl,
        int averageMinutes
) {
    public static GameDto toDto(Game game) {
        return new GameDto(
                game.getGameId().uuid().toString(),
                game.getName(),
                game.getRules(),
                game.getPictureUrl(),
                game.getCategory(),
                game.getGameUrl(),
                game.getAverageMinutes()
        );
    }
}