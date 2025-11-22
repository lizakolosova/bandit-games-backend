package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendDto(
        UUID friendId,
        String username,
        String pictureUrl
) {}

