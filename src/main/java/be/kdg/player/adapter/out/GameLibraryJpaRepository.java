package be.kdg.player.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameLibraryJpaRepository extends JpaRepository<GameLibraryJpaEntity, UUID> {
}

