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

    private int matchesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDraw;

    private LocalDateTime purchasedAt;
    private String stripePaymentIntentId;

    public GameLibrary(UUID gameId) {
        this.gameLibraryId = GameLibraryId.create();
        this.gameId = gameId;
        this.addedAt = LocalDateTime.now();
        this.totalPlaytime = Duration.ZERO;
        this.isFavourite = false;
        this.purchasedAt = purchasedAt;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.matchesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesDraw = 0;
    }

    public GameLibrary(GameLibraryId gameLibraryId,
                       UUID gameId,
                       LocalDateTime addedAt,
                       LocalDateTime lastPlayedAt,
                       Duration totalPlaytime,
                       boolean isFavourite,
                       LocalDateTime purchasedAt,
                       String stripePaymentIntentId) {
        this.gameLibraryId = gameLibraryId;
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.lastPlayedAt = lastPlayedAt;
        this.totalPlaytime = totalPlaytime != null ? totalPlaytime : Duration.ZERO;
        this.isFavourite = isFavourite;
    }

    public GameLibrary(GameLibraryId gameLibraryId, UUID gameId, LocalDateTime addedAt, LocalDateTime lastPlayedAt, Duration totalPlaytime, boolean isFavourite, int matchesPlayed, int gamesWon, int gamesLost, int gamesDraw) {
        this.gameLibraryId = gameLibraryId;
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.lastPlayedAt = lastPlayedAt;
        this.totalPlaytime = totalPlaytime;
        this.isFavourite = isFavourite;
        this.matchesPlayed = matchesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDraw = gamesDraw;
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

    public void recordMatchResult(UUID playerId, LocalDateTime startedAt, LocalDateTime finishedAt, UUID winnerPlayerId) {
        this.updateLastPlayed(LocalDateTime.now());
        this.matchesPlayed++;

        if (startedAt != null && finishedAt != null) {
            this.increasePlaytime(Duration.between(startedAt, finishedAt));
        }

        if (winnerPlayerId == null) {
            this.gamesDraw++;
        } else if (winnerPlayerId.equals(playerId)) {
            this.gamesWon++;
        } else {
            this.gamesLost++;
        }
    }

    public boolean isPurchased() {
        return purchasedAt != null;
    }

    public void markAsPurchased(String paymentIntentId) {
        this.purchasedAt = LocalDateTime.now();
        this.stripePaymentIntentId = paymentIntentId;
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

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesDraw() {
        return gamesDraw;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
}