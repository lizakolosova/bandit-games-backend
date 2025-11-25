package be.kdg.platform.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementDefinitionJpaRepository extends JpaRepository<AchievementDefinitionJpaEntity, UUID> {
}

