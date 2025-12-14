package be.kdg.player.port.out;

import java.util.UUID;

public interface SaveGameLibraryPort {
    void addPurchasedGame(UUID playerId, UUID gameId, String paymentIntentId);
}