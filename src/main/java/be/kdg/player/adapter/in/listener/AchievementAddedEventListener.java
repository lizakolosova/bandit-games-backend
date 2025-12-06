package be.kdg.player.adapter.in.listener;

import be.kdg.common.events.AchievementAddedEvent;
import be.kdg.player.port.in.AchievementProjectionProjector;
import be.kdg.player.port.in.command.AchievementAddedProjectionCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AchievementAddedEventListener {

    private final AchievementProjectionProjector projector;

    public AchievementAddedEventListener(AchievementProjectionProjector projector) {
        this.projector = projector;
    }

    @EventListener
    public void onAchievementsAdded(AchievementAddedEvent event) {
        projector.project(new AchievementAddedProjectionCommand(
                event.achievementId().toString(),
                event.gameId().toString(),
                event.name(),
                event.description()
        ));
    }
}

