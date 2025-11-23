package be.kdg.player.domain;

import be.kdg.common.exception.NotFoundException;
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

    private Set<GameLibrary> gameLibraries = new HashSet<>();
    private Set<Friend> friends = new HashSet<>();
    private Set<PlayerAchievement> achievements = new HashSet<>();

    public Player(PlayerId playerId, String username, String email, String pictureUrl) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.createdAt = LocalDateTime.now();
    }

    public Player(PlayerId playerId, String username, String email, String pictureUrl, LocalDateTime createdAt) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
    }

    public void addFriend(UUID friendId) {
        friends.add(new Friend(friendId, LocalDateTime.now()));
        // we'll have an event here
    }

    public GameLibrary addGameToLibrary(UUID gameId) {
        GameLibrary library = new GameLibrary(gameId);
        gameLibraries.add(library);
        return library;
        // we'll have an event here
    }

    public boolean canPlay(UUID gameId) {
        return gameLibraries.stream()
                .anyMatch(f -> f.getGameId().equals(gameId));
    }

    public void addToFavourites(UUID gameId) {
        GameLibrary entry = findGameInLibrary(gameId);
        if (entry == null) {
            throw NotFoundException.game(gameId);
        }
        entry.markAsFavourite();
    }

    public GameLibrary findGameInLibrary(UUID gameId) {
        return gameLibraries.stream()
                .filter(g -> g.getGameId().equals(gameId))
                .findFirst()
                .orElse(null);
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

    public Set<GameLibrary> getGameLibraries() {
        return gameLibraries;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public Set<PlayerAchievement> getAchievements() {
        return achievements;
    }
}

