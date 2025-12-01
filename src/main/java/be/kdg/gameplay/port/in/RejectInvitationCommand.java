package be.kdg.gameplay.port.in;

import java.util.UUID;

public record RejectInvitationCommand(UUID gameRoomId, UUID playerId) {}
