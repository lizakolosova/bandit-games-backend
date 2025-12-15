package be.kdg.player.adapter.out;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_game_library", schema = "kdg_player")
public class GameLibraryJpaEntity {

    @Id
    private UUID id;

    private UUID gameId;

    private LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;

    private Long totalPlaytimeSeconds;

    private boolean favourite;

    private LocalDateTime purchasedAt;  
    private String stripePaymentIntentId;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;

    private int matchesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDraw;

    protected GameLibraryJpaEntity() {}

    public GameLibraryJpaEntity(UUID id, UUID gameId, LocalDateTime addedAt, Long totalPlaytimeSeconds, LocalDateTime lastPlayedAt, boolean favourite, PlayerJpaEntity player, int matchesPlayed, int gamesWon, int gamesLost, int gamesDraw) {
        this.id = id;
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.totalPlaytimeSeconds = totalPlaytimeSeconds;
        this.lastPlayedAt = lastPlayedAt;
        this.favourite = favourite;
        this.player = player;
        this.matchesPlayed = matchesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDraw = gamesDraw;
    }

    public GameLibraryJpaEntity(UUID id, UUID gameId, LocalDateTime addedAt, LocalDateTime lastPlayedAt, Long totalPlaytimeSeconds, boolean favourite, LocalDateTime purchasedAt, String stripePaymentIntentId, PlayerJpaEntity player) {
        this.id = id;
        this.gameId = gameId;
        this.addedAt = addedAt;
        this.lastPlayedAt = lastPlayedAt;
        this.totalPlaytimeSeconds = totalPlaytimeSeconds;
        this.favourite = favourite;
        this.purchasedAt = purchasedAt;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.player = player;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public Long getTotalPlaytimeSeconds() {
        return totalPlaytimeSeconds;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public PlayerJpaEntity getPlayer() {
        return player;
    }

    public LocalDateTime getPurchasedAt() {  
        return purchasedAt;
    }

    public String getStripePaymentIntentId() {  
        return stripePaymentIntentId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesDraw() {
        return gamesDraw;
    }

    public void setPlayer(PlayerJpaEntity player) {
        this.player = player;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {  
        this.purchasedAt = purchasedAt;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {  
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
}