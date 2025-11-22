package be.kdg.common.exception;

public class MatchException extends RuntimeException {
    public MatchException(final String message) {
        super(message);
    }
    public static MatchException notActive() {
        return new MatchException("This match is not active");
    }
}
