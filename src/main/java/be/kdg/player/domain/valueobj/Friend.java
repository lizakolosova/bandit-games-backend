package be.kdg.player.domain.valueobj;

import java.time.LocalDateTime;
import java.util.UUID;

public record Friend(UUID friendId, LocalDateTime since) { }

