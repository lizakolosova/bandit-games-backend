package be.kdg.common.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }

    public static NotFoundException game(final UUID id) {
        return new NotFoundException(String.format("Game with ID  %s was not found.", id));
    }

    public static NotFoundException player(final UUID id) {
        return new NotFoundException(String.format("Player with ID  %s was not found.", id));
    }
}
