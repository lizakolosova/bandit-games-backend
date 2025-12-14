package be.kdg.player.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.PurchaseGameResponse;
import be.kdg.player.port.in.PurchaseGameUseCase;
import be.kdg.player.port.out.LoadGameProjectionPort;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.PaymentPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import be.kdg.player.port.out.SaveGameLibraryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PurchaseGameUseCaseImpl implements PurchaseGameUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final LoadGameProjectionPort loadGameProjectionPort;
    private final PaymentPort paymentPort;
    private final UpdatePlayerPort updatePlayerPort;
    private final SaveGameLibraryPort saveGameLibraryPort;

    public PurchaseGameUseCaseImpl(LoadPlayerPort loadPlayerPort,
                                   LoadGameProjectionPort loadGameProjectionPort,
                                   PaymentPort paymentPort,
                                   UpdatePlayerPort updatePlayerPort,
                                   SaveGameLibraryPort saveGameLibraryPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.loadGameProjectionPort = loadGameProjectionPort;
        this.paymentPort = paymentPort;
        this.updatePlayerPort = updatePlayerPort;
        this.saveGameLibraryPort = saveGameLibraryPort;
    }

    @Override
    public PurchaseGameResponse initiateGamePurchase(UUID playerId, UUID gameId) {
        Player player = loadPlayerPort.loadById(PlayerId.of(playerId))
                .orElseThrow(() -> new RuntimeException("Player not found"));

        GameProjection game = loadGameProjectionPort.loadProjection(GameId.of(gameId));

        if (game.getPrice() == null) {
            throw new RuntimeException("Game has no price");
        }

        var existingLibrary = player.findGameInLibrary(gameId);
        if (existingLibrary != null && existingLibrary.isPurchased()) {
            throw new RuntimeException("Game already purchased");
        }

        try {
            var paymentIntent = paymentPort.createPaymentIntent(
                    playerId, gameId, game.getPrice());

            return new PurchaseGameResponse(
                    paymentIntent.clientSecret(),
                    paymentIntent.paymentIntentId(),
                    paymentIntent.amountInCents()
            );
        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed", e);
        }
    }

    // PurchaseGameUseCaseImpl.java

    @Override
    @Transactional
    public void confirmGamePurchase(String paymentIntentId, Map<String, String> metadata) {
        try {
            // 1. Extract IDs from the metadata provided by the webhook
            String playerIdStr = metadata.get("playerId");
            String gameIdStr = metadata.get("gameId");

            if (playerIdStr == null || gameIdStr == null) {
                throw new RuntimeException("Missing metadata in PaymentIntent: " + paymentIntentId);
            }

            UUID playerId = UUID.fromString(playerIdStr);
            UUID gameId = UUID.fromString(gameIdStr);

            // 2. Persist to the library via the Port
            saveGameLibraryPort.addPurchasedGame(playerId, gameId, paymentIntentId);

            System.out.println("Game " + gameId + " added to library for player " + playerId);

        } catch (Exception e) {
            System.err.println("Error confirming purchase: " + e.getMessage());
            // Since this is @Transactional, any exception will rollback DB changes
            throw new RuntimeException("Payment confirmation failed", e);
        }
    }
}