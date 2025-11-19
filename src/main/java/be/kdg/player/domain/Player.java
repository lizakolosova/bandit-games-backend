package be.kdg.player.domain;

import be.kdg.player.domain.valueobj.FavouriteGame;
import be.kdg.player.domain.valueobj.Friend;
import be.kdg.common.valueobj.PlayerId;

import java.time.LocalDateTime;
import java.util.*;

public class Player {

    private PlayerId playerId;
    private String username;
    private String email;
    private String pictureUrl;
    private LocalDateTime createdAt;

    private final Set<FavouriteGame> favouriteGames = new HashSet<>();
    private final Set<Friend> friends = new HashSet<>();
    private final Set<PlayerAchievement> achievements = new HashSet<>();

    public Player(PlayerId playerId, String username, String email, String pictureUrl) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void addFriend(UUID friendId) {
        friends.add(new Friend(friendId, LocalDateTime.now()));
        // we'll have an event here
    }

    public void addFavouriteGame(UUID gameId) {
        favouriteGames.add(new FavouriteGame(gameId, LocalDateTime.now(), null, null));
        // we'll have an event here
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Set<FavouriteGame> getFavouriteGames() {
        return favouriteGames;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public Set<PlayerAchievement> getAchievements() {
        return achievements;
    }
}

