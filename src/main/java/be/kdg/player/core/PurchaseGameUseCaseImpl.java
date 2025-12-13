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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PurchaseGameUseCaseImpl implements PurchaseGameUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final LoadGameProjectionPort loadGameProjectionPort;
    private final PaymentPort paymentPort;
    private final UpdatePlayerPort updatePlayerPort;

    public PurchaseGameUseCaseImpl(LoadPlayerPort loadPlayerPort,
                                   LoadGameProjectionPort loadGameProjectionPort,
                                   PaymentPort paymentPort,
                                   UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.loadGameProjectionPort = loadGameProjectionPort;
        this.paymentPort = paymentPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public PurchaseGameResponse initiateGamePurchase(UUID playerId, UUID gameId) {
        // Get player
        Player player = loadPlayerPort.loadById(PlayerId.of(playerId))  // Convert UUID → PlayerId
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Get game and price
        GameProjection game = loadGameProjectionPort.loadProjection(GameId.of(gameId));



        if (game.getPrice() == null) {
            throw new RuntimeException("Game has no price");
        }

        // Check if already purchased
        var existingLibrary = player.findGameInLibrary(gameId);
        if (existingLibrary != null && existingLibrary.isPurchased()) {
            throw new RuntimeException("Game already purchased");
        }

        // Create payment intent via Stripe
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

    @Override
    @Transactional
    public void confirmGamePurchase(String paymentIntentId) {
        // Get payment metadata from Stripe
        try {
            var metadata = paymentPort.getPaymentMetadata(paymentIntentId);
            UUID playerId = UUID.fromString(metadata.get("playerId"));
            UUID gameId = UUID.fromString(metadata.get("gameId"));

            // Load player
            Player player = loadPlayerPort.loadById(PlayerId.of(playerId))  // Convert UUID → PlayerId
                    .orElseThrow(() -> new RuntimeException("Player not found"));

            // Purchase game
            player.purchaseGame(gameId, paymentIntentId);

            // Save player
            updatePlayerPort.update(player);

        } catch (Exception e) {
            throw new RuntimeException("Payment confirmation failed", e);
        }
    }
}