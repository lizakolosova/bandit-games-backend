package be.kdg.gameplay.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, UUID> {
    Optional<MatchJpaEntity> findFirstByPlayersContainingOrderByStartedAtDesc(UUID playerId);

    @Query("select m from MatchJpaEntity m where :playerId member of m.players and m.status = 'FINISHED'")
    List<MatchJpaEntity> findAllByPlayerId(UUID playerId);
}