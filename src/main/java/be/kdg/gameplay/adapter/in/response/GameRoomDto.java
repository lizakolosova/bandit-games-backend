package be.kdg.gameplay.adapter.in.response;

import be.kdg.gameplay.domain.GameRoom;

import java.util.UUID;

public record GameRoomDto(
        UUID gameRoomId,
        UUID gameId,
        UUID hostPlayerId,
        UUID invitedPlayerId,
        String status,
        String type
) {
    public static GameRoomDto from(GameRoom room) {
        return new GameRoomDto(
                room.getGameRoomId().uuid(),
                room.getGameId().uuid(),
                room.getHostPlayerId().uuid(),
                room.getInvitedPlayerId() == null ? null : room.getInvitedPlayerId().uuid(),
                room.getStatus().name(),
                room.getGameRoomType().name()
        );
    }
}

