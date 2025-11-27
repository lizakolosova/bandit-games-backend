package be.kdg.player.port.in;

import java.util.UUID;

public record RegisterPlayerCommand(String username,
                                    String email) {
}
