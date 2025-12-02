package be.kdg.gameplay.adapter.out;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "game_projection")
public class GameViewProjectionJpaEntity {

    @Id
    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    protected GameViewProjectionJpaEntity() {
    }

    public GameViewProjectionJpaEntity(UUID gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public UUID getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}

