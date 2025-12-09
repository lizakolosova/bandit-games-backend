package be.kdg.gameplay.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, UUID> {
    @Query("SELECT m FROM MatchJpaEntity m WHERE :playerId MEMBER OF m.players")
    List<MatchJpaEntity> findByPlayerId(@Param("playerId") UUID playerId);
}