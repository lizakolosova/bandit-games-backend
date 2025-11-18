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
        favouriteGames.add(new FavouriteGame(gameId, LocalDateTime.now()));
        // we'll have an event here
    }
}

