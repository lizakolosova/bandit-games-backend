package be.kdg.gameplay.adapter.out;

import be.kdg.common.events.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component
public class GameplayEventPublisher {

    private final ApplicationEventPublisher springPublisher;

    public GameplayEventPublisher(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }

    public void publishEvents(List<DomainEvent> domainEvents) {
        domainEvents.forEach(springPublisher::publishEvent);
    }
}

