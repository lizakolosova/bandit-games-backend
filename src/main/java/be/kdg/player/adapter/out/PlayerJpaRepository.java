package be.kdg.player.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerJpaRepository extends JpaRepository<PlayerJpaEntity, UUID> {
    @Query("SELECT p FROM PlayerJpaEntity p WHERE p.uuid <> :excludeId")
    List<PlayerJpaEntity> findAllExcept(UUID excludeId);

    @Query("""
            SELECT p FROM PlayerJpaEntity p 
            WHERE p.uuid <> :excludeId 
            AND LOWER(p.username) LIKE %:query%
            """)
    List<PlayerJpaEntity> searchByUsername(UUID excludeId, String query);
}