package be.kdg.player.adapter.in;

import be.kdg.player.port.in.PurchaseGameResponse;
import be.kdg.player.port.in.PurchaseGameUseCase;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PurchaseGameUseCase purchaseGameUseCase;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.webhook.verification.enabled:true}")
    private boolean webhookVerificationEnabled;

    public PaymentController(PurchaseGameUseCase purchaseGameUseCase) {
        this.purchaseGameUseCase = purchaseGameUseCase;
    }

    @PostMapping("/initiate-purchase")
    public ResponseEntity<PurchaseGameResponse> initiatePurchase(
            @RequestBody InitiatePurchaseRequest request) {

        try {
            PurchaseGameResponse response = purchaseGameUseCase
                    .initiateGamePurchase(request.playerId(), request.gameId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {

        System.out.println("=== WEBHOOK RECEIVED ===");
        System.out.println("Verification enabled: " + webhookVerificationEnabled);
        System.out.println("Payload: " + payload);

        Event event;

        try {
            if (webhookVerificationEnabled) {
                // Production: Verify webhook signature
                System.out.println("Verifying signature: " + sigHeader);
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } else {
                // Test mode: Skip signature verification
                System.out.println(" Skipping signature verification (test mode)");
                event = Event.GSON.fromJson(payload, Event.class);
            }
            System.out.println("Event type: " + event.getType());
        } catch (SignatureVerificationException e) {
            System.err.println("Signature verification failed: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Webhook error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            // Helper to handle the deserialization quirk discussed earlier
            PaymentIntent paymentIntent = event.getDataObjectDeserializer().getObject()
                    .map(obj -> (PaymentIntent) obj)
                    .orElseGet(() -> Event.GSON.fromJson(
                            event.getDataObjectDeserializer().getRawJson(),
                            PaymentIntent.class));

            // Get metadata directly from the object
            var metadata = paymentIntent.getMetadata();
            String piId = paymentIntent.getId();

            try {
                // Pass the metadata map directly to the use case
                purchaseGameUseCase.confirmGamePurchase(piId, metadata);
                System.out.println("Purchase confirmed successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to process payment");
            }
        }

        return ResponseEntity.ok("Success");
    }
}

record InitiatePurchaseRequest(UUID playerId, UUID gameId) {}