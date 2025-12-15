package be.kdg.player.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.PurchaseGameResponse;
import be.kdg.player.port.in.PurchaseGameUseCase;
import be.kdg.player.port.out.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class PurchaseGameUseCaseImpl implements PurchaseGameUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final LoadGameProjectionPort loadGameProjectionPort;
    private final PaymentPort paymentPort;
    private final UpdatePlayerPort updatePlayerPort;

    public PurchaseGameUseCaseImpl(LoadPlayerPort loadPlayerPort, LoadGameProjectionPort loadGameProjectionPort,
                                   PaymentPort paymentPort, UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.loadGameProjectionPort = loadGameProjectionPort;
        this.paymentPort = paymentPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public PurchaseGameResponse initiateGamePurchase(UUID playerId, UUID gameId) throws PaymentException {
        Player player = loadPlayerPort.loadById(PlayerId.of(playerId))
                .orElseThrow(() -> new RuntimeException("Player not found"));

        GameProjection game = loadGameProjectionPort.loadProjection(GameId.of(gameId));
        if (game.getPrice() == null) throw new RuntimeException("Game has no price");

        PaymentIntent paymentIntent = paymentPort.createPaymentIntent(playerId, gameId, game.getPrice());

        player.startPurchase(gameId, paymentIntent.paymentIntentId());
        updatePlayerPort.update(player);

        return new PurchaseGameResponse(
                paymentIntent.clientSecret(),
                paymentIntent.paymentIntentId(),
                paymentIntent.amountInCents()
        );
    }

    @Override
    @Transactional
    public void confirmGamePurchase(String paymentIntentId, Map<String, String> metadata) {
        UUID playerId = UUID.fromString(metadata.get("playerId"));
        UUID gameId = UUID.fromString(metadata.get("gameId"));

        Player player = loadPlayerPort.loadById(PlayerId.of(playerId))
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.confirmPurchase(gameId, paymentIntentId);
        updatePlayerPort.update(player);
    }
}