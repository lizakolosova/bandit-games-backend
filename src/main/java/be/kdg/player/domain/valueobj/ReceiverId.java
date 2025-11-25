package be.kdg.player.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

import java.util.UUID;

public record ReceiverId(UUID uuid) {
    public static ReceiverId of(PlayerId uuid) {
        return new ReceiverId(uuid.uuid());
    }
    public static ReceiverId of(UUID uuid) {
        return new ReceiverId(uuid);
    }
}
