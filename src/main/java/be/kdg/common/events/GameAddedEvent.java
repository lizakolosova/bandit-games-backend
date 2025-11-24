package be.kdg.common.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GameAddedEvent(
        LocalDateTime occurredAt,
        UUID gameId,
        String name,
        String rules,
        String pictureUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int averageMinutes,
        int achievementCount
) implements DomainEvent {
    public GameAddedEvent(UUID gameId, String name, String rules, String pictureUrl, String category, String developedBy,
                          LocalDate createdAt, int averageMinutes, int achievementCount) {
        this(LocalDateTime.now(), gameId, name, rules, pictureUrl, category, developedBy, createdAt, averageMinutes, achievementCount);

    }
    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

