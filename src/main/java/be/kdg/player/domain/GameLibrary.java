package be.kdg.player.domain;

import be.kdg.player.domain.valueobj.GameLibraryId;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class GameLibrary {

    private GameLibraryId gameLibraryId;
    private UUID gameId;
    private  LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;
    private Duration totalPlaytime;
    private boolean isFavourite;

    public GameLibrary(UUID gameId) {
        this.gameLibraryId = GameLibraryId.create();
        this.gameId = gameId;
        this.addedAt = LocalDateTime.now();
        this.totalPlaytime = Duration.ZERO;
        this.isFavourite = false;
    }

    public GameLibrary(GameLibraryId gameLibraryId,
                               UUID gameId,
                       LocalDateTime addedAt,
                       LocalDateTime lastPlayedAt,
                       Duration totalPlaytime,
                       boolean isFavourite) {
        this.gameLibraryId = gameLibraryId;
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.lastPlayedAt = lastPlayedAt;
        this.totalPlaytime = totalPlaytime != null ? totalPlaytime : Duration.ZERO;
        this.isFavourite = isFavourite;
    }

    public void markAsFavourite() {
        this.isFavourite = true;
    }

    public void unmarkAsFavourite() {
        this.isFavourite = false;
    }

    public void updateLastPlayed(LocalDateTime when) {
        this.lastPlayedAt = when;
    }

    public void increasePlaytime(Duration duration) {
        this.totalPlaytime = this.totalPlaytime.plus(duration);
    }

    public UUID getGameId() {
        return gameId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public Duration getTotalPlaytime() {
        return totalPlaytime;
    }

    public GameLibraryId getGameLibraryId() {
        return gameLibraryId;
    }
}
