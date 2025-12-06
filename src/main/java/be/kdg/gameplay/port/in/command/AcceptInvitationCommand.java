package be.kdg.gameplay.port.in.command;

import java.util.UUID;

public record AcceptInvitationCommand(UUID gameRoomId, UUID playerId) {}
