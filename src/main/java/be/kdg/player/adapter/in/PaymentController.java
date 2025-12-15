package be.kdg.player.adapter.in;

import be.kdg.player.adapter.in.request.InitiatePurchaseRequest;
import be.kdg.player.port.in.PurchaseGameUseCase;
import be.kdg.player.port.in.command.PurchaseGameResponse;
import be.kdg.player.port.out.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PurchaseGameUseCase purchaseGameUseCase;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    @Value("${stripe.webhook.verification.enabled:true}")
    private boolean webhookVerificationEnabled;

    public PaymentController(PurchaseGameUseCase purchaseGameUseCase) {
        this.purchaseGameUseCase = purchaseGameUseCase;
    }

    @PostMapping("/initiate-purchase")
    public ResponseEntity<PurchaseGameResponse> initiatePurchase(@Valid @RequestBody InitiatePurchaseRequest request) throws PaymentException {
        return ResponseEntity.ok(purchaseGameUseCase.initiateGamePurchase(request.playerId(), request.gameId()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                              @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        final Event event;
        try {
            event = constructEvent(payload, sigHeader);
        } catch (SignatureVerificationException e) {
            log.warn("Stripe webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.warn("Stripe webhook payload invalid: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = deserializePaymentIntent(event);
            if (paymentIntent == null) return ResponseEntity.ok().build();

            try {
                purchaseGameUseCase.confirmGamePurchase(paymentIntent.getId(), paymentIntent.getMetadata());
            } catch (Exception e) {
                log.error("Failed to confirm purchase for paymentIntentId={}", paymentIntent.getId(), e);
            }
        }

        return ResponseEntity.ok().build();
    }

    private Event constructEvent(String payload, String sigHeader) throws Exception {
        if (!webhookVerificationEnabled) {
            log.warn("Stripe webhook signature verification is DISABLED.");
            return Event.GSON.fromJson(payload, Event.class);
        }
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("stripe.webhook.secret is not configured");
        }
        if (sigHeader == null || sigHeader.isBlank()) {
            throw new SignatureVerificationException("Missing Stripe-Signature header", null);
        }
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }

    private PaymentIntent deserializePaymentIntent(Event event) {
        try {
            return event.getDataObjectDeserializer().getObject()
                    .map(obj -> (PaymentIntent) obj)
                    .orElseGet(() -> Event.GSON.fromJson(
                            event.getDataObjectDeserializer().getRawJson(),
                            PaymentIntent.class));
        } catch (Exception e) {
            log.warn("Failed to deserialize PaymentIntent from event", e);
            return null;
        }
    }
}