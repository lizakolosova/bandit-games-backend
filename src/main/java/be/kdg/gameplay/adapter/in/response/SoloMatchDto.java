package be.kdg.gameplay.adapter.in.response;

import java.util.UUID;

public record SoloMatchDto(
        UUID matchId,
        UUID gameId,
        String status
) {}