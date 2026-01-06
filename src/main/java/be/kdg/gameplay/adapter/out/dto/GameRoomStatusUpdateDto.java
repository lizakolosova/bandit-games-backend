package be.kdg.gameplay.adapter.out.dto;

import java.util.UUID;

public record GameRoomStatusUpdateDto(
        UUID gameRoomId,
        String status,
        String invitationStatus,
        UUID invitedPlayerId
) {}
