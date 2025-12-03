package be.kdg.gameplay.adapter.in.response;

import java.util.UUID;

public record MatchDto(
        UUID matchId,
        UUID gameId,
        String status
) {}