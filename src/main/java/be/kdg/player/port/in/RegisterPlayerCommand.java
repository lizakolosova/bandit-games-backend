package be.kdg.player.port.in;

import java.util.UUID;

public record RegisterPlayerCommand(UUID playerId,
                                    String username,
                                    String email) {
}
