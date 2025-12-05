package be.kdg.gameplay.adapter.out.mapper;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.GameRoomJpaEntity;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;

public class GameRoomJpaMapper {

    public static GameRoom toDomain(GameRoomJpaEntity entity) {
        return new GameRoom(
                entity.getCreatedAt(),
                GameRoomId.of(entity.getUuid()),
                GameId.of(entity.getGameId()),
                entity.getHostPlayerName(),
                entity.getInvitedPlayerName(),
                PlayerId.of(entity.getHostPlayerId()),
                entity.getInvitedPlayerId() == null ? null : PlayerId.of(entity.getInvitedPlayerId()),
                entity.getGameRoomType(),
                entity.getStatus(),
                entity.getInvitationStatus()

        );
    }

    public static GameRoomJpaEntity toEntity(GameRoom domain) {

        return new GameRoomJpaEntity(
                domain.getGameRoomId().uuid(),
                domain.getGameId().uuid(),
                domain.getHostPlayerName(),
                domain.getInvitedPlayerName(),
                domain.getHostPlayerId().uuid(),
                domain.getInvitedPlayerId() == null ? null : domain.getInvitedPlayerId().uuid(),
                domain.getGameRoomType(),
                domain.getStatus(),
                domain.getCreatedAt(),
                domain.getInvitationStatus()
        );
    }
}