package be.kdg.player.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

import java.util.UUID;

public record SenderId(UUID uuid) {
    public static SenderId of(PlayerId uuid) {
        return new SenderId(uuid.uuid());
    }
    public static SenderId of(UUID uuid) {
        return new SenderId(uuid);
    }
}
