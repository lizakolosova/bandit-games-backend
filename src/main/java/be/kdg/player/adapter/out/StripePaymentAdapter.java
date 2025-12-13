package be.kdg.player.adapter.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.port.out.PaymentException;
import be.kdg.player.port.out.PaymentIntent;
import be.kdg.player.port.out.PaymentPort;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
public class StripePaymentAdapter implements PaymentPort {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(
            UUID playerId, UUID gameId, BigDecimal amount) throws PaymentException {

        try {
            long amountInCents = amount.multiply(new BigDecimal(100)).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("eur")
                    .putMetadata("playerId", playerId.toString())
                    .putMetadata("gameId", gameId.toString())
                    .build();

            com.stripe.model.PaymentIntent intent =
                    com.stripe.model.PaymentIntent.create(params);

            return new be.kdg.player.port.out.PaymentIntent(
                    intent.getClientSecret(),
                    intent.getId(),
                    amountInCents
            );

        } catch (StripeException e) {
            throw new PaymentException("Failed to create payment intent", e);
        }
    }
    @Override
    public Map<String, String> getPaymentMetadata(String paymentIntentId) throws PaymentException {
        try {
            com.stripe.model.PaymentIntent intent =
                    com.stripe.model.PaymentIntent.retrieve(paymentIntentId);
            return intent.getMetadata();
        } catch (StripeException e) {
            throw new PaymentException("Failed to retrieve payment metadata", e);
        }
    }
}