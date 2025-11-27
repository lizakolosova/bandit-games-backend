package be.kdg.gameplay.port.in;

import java.util.UUID;

public record StartMatchCommand(
        UUID gameRoomId
) {}
