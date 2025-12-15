package be.kdg.player.port.in.command;

public record PurchaseGameResponse(
        String clientSecret,
        String paymentIntentId,
        Long amountInCents
) {}