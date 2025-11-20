package be.kdg.player.domain.valueobj;

import java.util.UUID;

public record FriendshipRequestId(UUID uuid) {
    public static FriendshipRequestId of(UUID uuid) {
        return new FriendshipRequestId(uuid);
    }
    public static FriendshipRequestId create() {
        return new FriendshipRequestId(UUID.randomUUID());
    }
}
