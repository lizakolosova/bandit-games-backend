package be.kdg.gameplay.port.in;

import java.util.UUID;

public record FinalizeRoomCommand(
        UUID gameRoomId, UUID playerId
) {}
