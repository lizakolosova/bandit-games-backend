package be.kdg.player.domain.valueobj;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record FavouriteGame(UUID gameId, LocalDateTime addedAt, LocalDateTime lastPlayedAt, Duration totalPlaytime) { }

