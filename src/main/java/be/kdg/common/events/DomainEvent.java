package be.kdg.common.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime eventPit();
}
