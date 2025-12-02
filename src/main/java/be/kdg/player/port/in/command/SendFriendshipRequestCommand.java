package be.kdg.player.port.in.command;

import java.util.UUID;

public record SendFriendshipRequestCommand(UUID senderId, UUID receiverId) {}