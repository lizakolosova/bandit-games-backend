package be.kdg.platform.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AchievementDefinitionJpaRepository extends JpaRepository<AchievementDefinitionJpaEntity, UUID> {
}

