package be.kdg.gameplay.port.in;

import java.util.UUID;

public record AcceptInvitationCommand(UUID gameRoomId, UUID playerId) {}
