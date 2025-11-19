package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavouriteGameDto(
        UUID gameId,
        LocalDateTime lastPlayedAt,
        Long totalPlaytimeMinutes
) {}
