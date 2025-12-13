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
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("=== WEBHOOK RECEIVED ===");
        System.out.println("Payload: " + payload);
        System.out.println("Signature: " + sigHeader);

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            System.out.println("Event type: " + event.getType());
        } catch (SignatureVerificationException e) {
            System.err.println("Signature verification failed: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Webhook error: " + e.getMessage());
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            System.out.println("Processing payment_intent.succeeded");

            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Failed to deserialize payment intent"));

            System.out.println("Payment Intent ID: " + paymentIntent.getId());
            System.out.println("Metadata: " + paymentIntent.getMetadata());

            try {
                purchaseGameUseCase.confirmGamePurchase(paymentIntent.getId());
                System.out.println(" Purchase confirmed successfully!");
            } catch (Exception e) {
                System.err.println(" Failed to confirm purchase: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(500).body("Failed to process payment");
            }
        }

        return ResponseEntity.ok("Success");
    }
}

record InitiatePurchaseRequest(UUID playerId, UUID gameId) {}