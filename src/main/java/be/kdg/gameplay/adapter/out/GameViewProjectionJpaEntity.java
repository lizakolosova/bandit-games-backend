package be.kdg.gameplay.adapter.out;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "game_view_projection",  schema = "kdg_gameplay")
public class GameViewProjectionJpaEntity {

    @Id
    private UUID gameId;

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

