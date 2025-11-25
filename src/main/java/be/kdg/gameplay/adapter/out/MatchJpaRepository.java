package be.kdg.gameplay.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, UUID> {
}