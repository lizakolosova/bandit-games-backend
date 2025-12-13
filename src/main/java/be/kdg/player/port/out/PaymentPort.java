package be.kdg.player.port.out;

import be.kdg.common.valueobj.PlayerId;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface PaymentPort {
    PaymentIntent createPaymentIntent(UUID playerId, UUID gameId, BigDecimal amount) throws PaymentException;
    Map<String, String> getPaymentMetadata(String paymentIntentId) throws PaymentException;
}