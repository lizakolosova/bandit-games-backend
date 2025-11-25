package be.kdg.platform.adapter.out.mapper;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.platform.adapter.out.GameJpaEntity;
import be.kdg.platform.adapter.out.AchievementDefinitionJpaEntity;
import be.kdg.platform.domain.AchievementDefinition;
import be.kdg.platform.domain.Game;

import java.util.ArrayList;
import java.util.List;

public class GameWithAchievementsMapper {

    public static Game toDomain(GameJpaEntity entity) {

        Game game = new Game(
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

        List<AchievementDefinition> defs =
                entity.getAchievements().stream()
                        .map(GameWithAchievementsMapper::toDomainAchievement)
                        .toList();

        game.setAchievements(new ArrayList<>(defs));

        return game;
    }


    private static AchievementDefinition toDomainAchievement(AchievementDefinitionJpaEntity entity) {
        return new AchievementDefinition(
                AchievementId.of(entity.getUuid()),
                entity.getName(),
                entity.getDescription(),
                entity.getHowToUnlock()
        );
    }

    public static GameJpaEntity toEntity(Game game) {

        GameJpaEntity entity = new GameJpaEntity(
                game.getGameId().uuid(),
                game.getName(),
                game.getRules(),
                game.getPictureUrl(),
                game.getGameUrl(),
                game.getCategory(),
                game.getDevelopedBy(),
                game.getCreatedAt(),
                game.getAverageMinutes()
        );

        game.getAchievements().forEach(def -> {
            AchievementDefinitionJpaEntity jpaDef =
                    toEntityAchievement(def, entity);
            entity.getAchievements().add(jpaDef);
        });

        return entity;
    }


    private static AchievementDefinitionJpaEntity toEntityAchievement(
            AchievementDefinition def,
            GameJpaEntity game
    ) {
        return new AchievementDefinitionJpaEntity(
                def.getAchievementId().uuid(),
                def.getName(),
                def.getDescription(),
                def.getHowToUnlock(),
                game
        );
    }
}
