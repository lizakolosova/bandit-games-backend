package be.kdg.acl.translator;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.unified.UnifiedAchievementAchievedEvent;
import be.kdg.common.events.unified.UnifiedMatchCreatedEvent;
import be.kdg.common.events.unified.UnifiedMatchEndedEvent;
import be.kdg.common.events.unified.UnifiedMatchUpdatedEvent;
import be.kdg.common.valueobj.GameId;

public interface EventTranslator {
    UnifiedMatchCreatedEvent translateToMatchCreated(DomainEvent event, GameId gameId);
    UnifiedMatchUpdatedEvent translateToMatchUpdated(DomainEvent event);
    UnifiedMatchEndedEvent translateToMatchEnded(DomainEvent event);
    UnifiedAchievementAchievedEvent translateToAchievementAchieved(DomainEvent event);
    boolean canTranslate(DomainEvent event);
}
