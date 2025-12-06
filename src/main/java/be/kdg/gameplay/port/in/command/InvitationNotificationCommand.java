package be.kdg.gameplay.port.in.command;

import java.util.UUID;

public record InvitationNotificationCommand(UUID recipientId, UUID gameRoomId, UUID hostId) {}