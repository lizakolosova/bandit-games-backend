package be.kdg.player.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.FriendRemovedEvent;
import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.valueobj.Friend;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.common.exception.NotFoundException;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Player {

    private PlayerId playerId;
    private String username;
    private String email;
    private String pictureUrl;
    private LocalDateTime createdAt;

    private Set<GameLibrary> gameLibraries = new HashSet<>();
    private Set<Friend> friends = new HashSet<>();
    private Set<PlayerAchievement> achievements = new HashSet<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Player(String username, String email, String pictureUrl) {
        this(PlayerId.create(), username, email, pictureUrl, LocalDateTime.now());
    }

    public Player(PlayerId playerId, String username, String email, String pictureUrl, LocalDateTime createdAt) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
    }

    public Player() {
    }

    public void addFriend(UUID friendId) {
        friends.add(new Friend(friendId, LocalDateTime.now()));
    }

    public void removeFriend(UUID friendId) {
        boolean removed = friends.removeIf(f -> f.friendId().equals(friendId));
        if (!removed) {
            throw NotFoundException.player(friendId);
        }
        registerEvent(new FriendRemovedEvent(this.playerId.uuid(), friendId));
    }


    public GameLibrary addGameToLibrary(UUID gameId) {
        GameLibrary library = new GameLibrary(gameId);
        gameLibraries.add(library);
        return library;
    }

    public void markGameAsFavourite(UUID gameId) {
        GameLibrary entry = findGameInLibrary(gameId);
        if (entry == null) {
            throw NotFoundException.game(gameId);
        }
        entry.markAsFavourite();
    }

    public void unmarkGameAsFavourite(UUID gameId) {
        GameLibrary entry = findGameInLibrary(gameId);
        if (entry == null) {
            throw NotFoundException.game(gameId);
        }
        entry.unmarkAsFavourite();
    }

    public GameLibrary findGameInLibrary(UUID gameId) {
        return gameLibraries.stream()
                .filter(g -> g.getGameId().equals(gameId))
                .findFirst()
                .orElse(null);
    }

    public List<DomainEvent> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

//    public void recordGameResult(UUID gameId, String winner, String playerName) {
//        GameLibrary library = findGameInLibrary(gameId);
//        if (library == null) {
//            library = addGameToLibrary(gameId);
//        }
//        library.recordGamePlayed();
//
//        if ("DRAW".equals(winner)) {
//        library.recordDraw();
//        } else if (playerName.equals(winner)) {
//        library.recordWin();
//        } else {
//        library.recordLoss();
//        }
//    }

    public void unlockAchievement(AchievementId achievementId, GameId gameId) {
    boolean alreadyUnlocked = achievements.stream()
            .anyMatch(a -> a.getAchievementId().equals(achievementId));

    if (alreadyUnlocked) {return;}

    PlayerAchievement achievement = new PlayerAchievement(this.playerId, achievementId, gameId);

    achievements.add(achievement);
}

    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
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

