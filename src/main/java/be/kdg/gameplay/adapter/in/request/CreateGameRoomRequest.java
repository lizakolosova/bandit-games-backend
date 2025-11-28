package be.kdg.gameplay.adapter.in.request;

import java.util.UUID;

public record CreateGameRoomRequest(
        UUID gameId,
        UUID invitedPlayerId,
        String gameRoomType
) {}

