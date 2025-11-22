package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameLibraryDto(
        UUID gameId,
        LocalDateTime addedAt,
        LocalDateTime lastPlayedAt,
        long totalPlaytimeMinutes,
        boolean favourite
) {}