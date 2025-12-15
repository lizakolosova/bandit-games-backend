package be.kdg.gameplay.adapter.out.mapper;


import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.adapter.out.GameViewProjectionJpaEntity;
import be.kdg.gameplay.domain.GameViewProjection;

public class GameViewProjectionMapper {

    public static GameViewProjection toDomain(GameViewProjectionJpaEntity entity) {
        return new GameViewProjection(GameId.of(entity.getGameId()), entity.getName());
    }

    public static GameViewProjectionJpaEntity toEntity(GameViewProjection projection) {
        return new GameViewProjectionJpaEntity(
                projection.getGameId().uuid(),
                projection.getName()
        );
    }
}

