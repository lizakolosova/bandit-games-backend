package be.kdg.player.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameLibraryJpaRepository extends JpaRepository<GameLibraryJpaEntity, UUID> {
}

