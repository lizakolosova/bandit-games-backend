package be.kdg.player.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

import java.util.UUID;

public record PurchaseId(UUID uuid) {
    public static PurchaseId of(UUID uuid) {
        return new PurchaseId(uuid);
    }
    public static PurchaseId create() {
        return new PurchaseId(UUID.randomUUID());
    }
}
