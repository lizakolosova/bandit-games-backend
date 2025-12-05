package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.valueobj.GameRoomType;

import java.util.UUID;

public record CreateGameRoomCommand(
        UUID gameId,
        String hostPlayerName,
        String invitedPlayerName,
        UUID hostPlayerId,
        UUID invitedPlayerId,
        GameRoomType gameRoomType
) {}

