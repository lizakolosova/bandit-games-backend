package be.kdg.platform.adapter.out.mapper;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.adapter.out.GameJpaEntity;
import be.kdg.platform.domain.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public static Game toDomain(GameJpaEntity entity) {
        return new Game(
                GameId.of(entity.getUuid()),
                entity.getName(),
                entity.getRules(),
                entity.getPictureUrl(),
                entity.getGameUrl(),
                entity.getCategory(),
                entity.getDevelopedBy(),
                entity.getCreatedAt(),
                entity.getAverageMinutes()
        );
    }
    public static GameJpaEntity toEntity(Game game) {
        return new GameJpaEntity(
                game.getGameId().uuid(),
                game.getName(),
                game.getRules(),
                game.getPictureUrl(),
                game.getGameUrl(),
                game.getCategory(),
                game.getDevelopedBy(),
                game.getCreatedAt(),
                game.getAverageMinutes(),
                game.isApproved()
        );
    }
}