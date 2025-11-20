package be.kdg.player.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

public record ReceiverId(PlayerId uuid) {
    public static ReceiverId of(PlayerId uuid) {
        return new ReceiverId(uuid);
    }
}
