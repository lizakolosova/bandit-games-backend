package be.kdg.common.valueobj;

import java.util.UUID;

public record PlayerId(UUID uuid) {
    public static PlayerId of(UUID uuid) {
        return new PlayerId(uuid);
    }
    public static PlayerId create() {
        return new PlayerId(UUID.randomUUID());
    }
}
