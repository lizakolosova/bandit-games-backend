package be.kdg.player.port.in;

public record PurchaseGameResponse(
        String clientSecret,
        String paymentIntentId,
        Long amountInCents
) {}