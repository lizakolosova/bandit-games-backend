package be.kdg.player.adapter.out;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "player", schema = "kdg_player")
public class PlayerJpaEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String pictureUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            name = "player_favourite_games",
            schema = "kdg_player",
            joinColumns = @JoinColumn(name = "player_id")
    )
    private Set<FavouriteGameEmbeddable> favouriteGames = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "player_friends",
            schema = "kdg_player",
            joinColumns = @JoinColumn(name = "player_id")
    )
    private Set<FriendEmbeddable> friends = new HashSet<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerAchievementJpaEntity> achievements = new HashSet<>();

    protected PlayerJpaEntity() {}

    public PlayerJpaEntity(UUID uuid, String username, String email, String pictureUrl, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
    }

    public UUID getUuid() { return uuid; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPictureUrl() { return pictureUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Set<FavouriteGameEmbeddable> getFavouriteGames() { return favouriteGames; }
    public Set<FriendEmbeddable> getFriends() { return friends; }
    public Set<PlayerAchievementJpaEntity> getAchievements() { return achievements; }
}
