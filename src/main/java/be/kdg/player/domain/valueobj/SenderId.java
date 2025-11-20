package be.kdg.player.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

public record SenderId(PlayerId uuid) {
    public static SenderId of(PlayerId uuid) {
        return new SenderId(uuid);
    }
}
