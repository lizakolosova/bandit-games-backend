package be.kdg.gameplay.port.in.command;

import java.util.UUID;

public record RejectInvitationCommand(UUID gameRoomId, UUID playerId) {}
