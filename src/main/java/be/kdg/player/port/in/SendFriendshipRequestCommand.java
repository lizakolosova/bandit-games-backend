package be.kdg.player.port.in;

import java.util.UUID;

public record SendFriendshipRequestCommand(UUID senderId, UUID receiverId) {}