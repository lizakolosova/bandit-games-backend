package be.kdg.gameplay.adapter.in;

import be.kdg.common.events.FinalizeRoomEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class EventTestController {

    private final ApplicationEventPublisher eventPublisher;

    public EventTestController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        System.out.println("=== EventTestController INITIALIZED ===");
    }

    @GetMapping("/publish-match-started")
    public String publishTestEventGet() {
        return publishEvent();
    }

    @PostMapping("/publish-match-started")
    public String publishTestEvent() {
        return publishEvent();
    }

    private String publishEvent() {
        System.out.println("=== ENDPOINT HIT ===");

        FinalizeRoomEvent event = new FinalizeRoomEvent(
                "hostPlayerName",
                "opponentPlayerName",
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        System.out.println(">>> Publishing FinalizeRoomEvent");
        System.out.println("    hostPlayerName: " + event.hostPlayerName());
        System.out.println("    opponentPlayerName: " + event.opponentPlayerName());
        System.out.println("    HostPlayer ID: " + event.hostPlayerId());
        System.out.println("    Opponent player ID: " + event.opponentPlayerId());

        eventPublisher.publishEvent(event);

        System.out.println(">>> Event published to Spring EventBus");

        return "Event published - Match: " + event.hostPlayerName() + ", Game: " + event.opponentPlayerName()
                + ", HostPlayer: " + event.hostPlayerId()
                +  ", OpponentPlayer: " + event.opponentPlayerId();
    }
}