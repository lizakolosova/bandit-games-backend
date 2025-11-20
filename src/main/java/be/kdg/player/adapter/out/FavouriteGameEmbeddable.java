package be.kdg.player.adapter.out;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
public class FavouriteGameEmbeddable {

    private UUID gameId;

    private LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;

    private Long totalPlaytimeSeconds;

    protected FavouriteGameEmbeddable() {}

    public FavouriteGameEmbeddable(UUID gameId,
                                   LocalDateTime addedAt,
                                   LocalDateTime lastPlayedAt,
                                   Long totalPlaytimeSeconds) {
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.lastPlayedAt = lastPlayedAt;
        this.totalPlaytimeSeconds = totalPlaytimeSeconds;
    }

    public UUID getGameId() { return gameId; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public LocalDateTime getLastPlayedAt() { return lastPlayedAt; }
    public Long getTotalPlaytimeSeconds() { return totalPlaytimeSeconds; }
}

