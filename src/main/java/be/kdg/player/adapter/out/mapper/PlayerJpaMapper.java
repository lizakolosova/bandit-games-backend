package be.kdg.player.adapter.out.mapper;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.*;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.PlayerAchievement;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.valueobj.Friend;
import be.kdg.player.domain.valueobj.GameLibraryId;

import java.time.Duration;

public class PlayerJpaMapper {

    public static Player toDomain(PlayerJpaEntity entity) {

        Player domain = new Player(
                PlayerId.of(entity.getUuid()),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPictureUrl()
        );

        entity.getGameLibraries().forEach(gl -> {

            GameLibrary lib = new GameLibrary(
                    GameLibraryId.of(gl.getId()),
                    gl.getGameId(),
                    gl.getAddedAt(),
                    gl.getLastPlayedAt(),
                    gl.getTotalPlaytimeSeconds() == null
                            ? Duration.ZERO
                            : Duration.ofSeconds(gl.getTotalPlaytimeSeconds()),
                    gl.isFavourite()
            );

            domain.getGameLibraries().add(lib);
        });

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

    public static PlayerJpaEntity toEntity(Player domain) {

        PlayerJpaEntity entity = new PlayerJpaEntity(
                domain.getPlayerId().uuid(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getPictureUrl(),
                domain.getCreatedAt()
        );

        domain.getGameLibraries().forEach(gl -> {

            GameLibraryJpaEntity jpa = new GameLibraryJpaEntity(
                    gl.getGameLibraryId().uuid(),
                    entity,
                    gl.getGameId()
            );

            jpa.setAddedAt(gl.getAddedAt());
            jpa.setLastPlayedAt(gl.getLastPlayedAt());
            jpa.setTotalPlaytimeSeconds(
                    gl.getTotalPlaytime() == null
                            ? 0L
                            : gl.getTotalPlaytime().getSeconds()
            );
            jpa.setFavourite(gl.isFavourite());

            entity.getGameLibraries().add(jpa);
        });

        domain.getFriends().forEach(f ->
                entity.getFriends().add(
                        new FriendEmbeddable(f.friendId(), f.since())
                )
        );

        domain.getAchievements().forEach(a -> {
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