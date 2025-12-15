package be.kdg.player.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AchievementProjectionJpaRepository extends JpaRepository<AchievementProjectionJpaEntity, UUID> {
    Optional<AchievementProjectionJpaEntity> findByName(String name);
}
