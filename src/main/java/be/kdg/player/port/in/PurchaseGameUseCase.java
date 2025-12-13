package be.kdg.player.port.in;

import be.kdg.common.valueobj.PlayerId;

import java.util.UUID;

public interface PurchaseGameUseCase {
    PurchaseGameResponse initiateGamePurchase(UUID playerId, UUID gameId);
    void confirmGamePurchase(String paymentIntentId);
}