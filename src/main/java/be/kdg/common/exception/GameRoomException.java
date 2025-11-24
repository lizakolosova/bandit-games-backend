package be.kdg.common.exception;

public class GameRoomException extends RuntimeException {
    public GameRoomException(final String message) {
        super(message);
    }

    public static GameRoomException invite() {
        return new GameRoomException("This game room is invite-only");
    }

    public static GameRoomException notReady() {
        return new GameRoomException("This game room is not ready");
    }
}
