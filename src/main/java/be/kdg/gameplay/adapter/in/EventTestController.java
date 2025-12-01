package be.kdg.gameplay.adapter.in;

import be.kdg.common.events.MatchStartedEvent;
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

        MatchStartedEvent event = new MatchStartedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        System.out.println(">>> Publishing MatchStartedEvent");
        System.out.println("    Match ID: " + event.matchId());
        System.out.println("    Game ID: " + event.gameId());

        eventPublisher.publishEvent(event);

        System.out.println(">>> Event published to Spring EventBus");

        return "Event published - Match: " + event.matchId() + ", Game: " + event.gameId();
    }
}