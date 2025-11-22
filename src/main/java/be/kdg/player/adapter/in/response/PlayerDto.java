package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerDto(
        UUID playerId,
        String username,
        String email,
        String pictureUrl,
        LocalDateTime createdAt
) {
}
