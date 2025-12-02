package be.kdg.gameplay.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameViewProjectionJpaRepository extends JpaRepository<GameViewProjectionJpaEntity, UUID> {
    Optional<GameViewProjectionJpaEntity> findByName(String name);
}

