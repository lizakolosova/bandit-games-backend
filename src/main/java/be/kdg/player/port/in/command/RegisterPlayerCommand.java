package be.kdg.player.port.in.command;

public record RegisterPlayerCommand(String username,
                                    String email,
                                    String password,
                                    String pictureUrl) {
}
