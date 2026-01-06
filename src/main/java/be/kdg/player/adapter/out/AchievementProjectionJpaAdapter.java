package be.kdg.player.adapter.out;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.AchievementProjection;
import be.kdg.player.port.out.AddAchievementProjectionPort;
import be.kdg.player.port.out.LoadAchievementProjectionPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AchievementProjectionJpaAdapter implements AddAchievementProjectionPort, LoadAchievementProjectionPort {
    private final AchievementProjectionJpaRepository achievements;
    private final GameProjectionJpaRepository games;

    public AchievementProjectionJpaAdapter(AchievementProjectionJpaRepository achievements, GameProjectionJpaRepository games) {
        this.achievements = achievements;
        this.games = games;
    }

    @Override
    public void add(AchievementProjection projection) {
        GameProjectionJpaEntity gameProjectionJpa = games.findById(projection.getGameId().uuid())
                .orElseThrow(() -> new IllegalStateException("Game not found: " + projection.getGameId()));

        AchievementProjectionJpaEntity entity =
                new AchievementProjectionJpaEntity(
                        projection.getAchievementId().uuid(),
                        projection.getName(),
                        projection.getDescription(),
                        gameProjectionJpa
                );

        achievements.save(entity);
    }

    @Override
    public Optional<AchievementProjection> loadByGameIdAndType(String achievementType) {
        return achievements.findByName(achievementType)
                .map(entity -> new AchievementProjection(
                        AchievementId.of(entity.getAchievementId()),
                        entity.getName(),
                        entity.getDescription(),
                        GameId.of(entity.getGameProjection().getGameId())
                ));
    }
}
