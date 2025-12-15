package be.kdg.player.port.in;

import be.kdg.player.port.in.command.PurchaseGameResponse;
import be.kdg.player.port.out.PaymentException;

import java.util.UUID;

public interface PurchaseGameUseCase {
    PurchaseGameResponse initiateGamePurchase(UUID playerId, UUID gameId) throws PaymentException;
    void confirmGamePurchase(String paymentIntentId, java.util.Map<String, String> metadata);
}