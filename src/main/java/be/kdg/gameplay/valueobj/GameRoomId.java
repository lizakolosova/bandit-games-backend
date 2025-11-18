package be.kdg.gameplay.valueobj;

public record GameRoomId(GameRoomId uuid) {
    public static GameRoomId of(GameRoomId uuid) {
        return new GameRoomId(uuid);
    }
}
