package be.kdg.player.port.out;

public record PaymentIntent(
        String clientSecret,
        String paymentIntentId,
        Long amountInCents
) {}