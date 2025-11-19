package be.kdg.player.adapter.out.mapper;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.*;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.PlayerAchievement;
import be.kdg.player.domain.valueobj.FavouriteGame;
import be.kdg.player.domain.valueobj.Friend;

import java.time.Duration;

public class PlayerJpaMapper {

    public Player toDomain(PlayerJpaEntity entity) {

        Player domain = new Player(
                PlayerId.of(entity.getUuid()),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPictureUrl()
        );

        entity.getFavouriteGames().forEach(f ->
                domain.getFavouriteGames().add(
                        new FavouriteGame(
                                f.getGameId(),
                                f.getAddedAt(),
                                f.getLastPlayedAt(),
                                f.getTotalPlaytimeSeconds() == null
                                        ? null
                                        : Duration.ofSeconds(f.getTotalPlaytimeSeconds())
                        )
                )
        );

        entity.getFriends().forEach(f ->
                domain.getFriends().add(
                        new Friend(f.getFriendId(), f.getSince())
                )
        );

        entity.getAchievements().forEach(a ->
                domain.getAchievements().add(
                        new PlayerAchievement(
                                PlayerId.of(entity.getUuid()),
                                new AchievementId(a.getAchievementId()),
                                new GameId(a.getGameId())
                        )
                )
        );

        return domain;
    }

    public PlayerJpaEntity toEntity(Player player) {

        PlayerJpaEntity entity = new PlayerJpaEntity(
                player.getPlayerId().uuid(),
                player.getUsername(),
                player.getEmail(),
                player.getPictureUrl(),
                player.getCreatedAt()
        );

        player.getFavouriteGames().forEach(f ->
                entity.getFavouriteGames().add(
                        new FavouriteGameEmbeddable(
                                f.gameId(),
                                f.addedAt(),
                                f.lastPlayedAt(),
                                f.totalPlaytime() == null ? null : f.totalPlaytime().toSeconds()
                        )
                )
        );

        player.getFriends().forEach(f ->
                entity.getFriends().add(
                        new FriendEmbeddable(f.friendId(), f.since())
                )
        );

        player.getAchievements().forEach(a -> {
            PlayerAchievementJpaEntity achEntity =
                    new PlayerAchievementJpaEntity(
                            a.getPlayerAchievementId().uuid(),
                            entity,
                            a.getAchievementId().uuid(),
                            a.getGameId().uuid(),
                            a.getUnlockedAt()
                    );
            entity.getAchievements().add(achEntity);
        });

        return entity;
    }
}
