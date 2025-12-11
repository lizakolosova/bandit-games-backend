package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameLibraryDetailsDto(
        UUID gameId,
        LocalDateTime addedAt,
        LocalDateTime lastPlayedAt,
        long totalPlaytimeMinutes,
        boolean favourite,
        int matchesPlayed,
        int gamesWon,
        int gamesLost,
        int gamesDraw
) {}

