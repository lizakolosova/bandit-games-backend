package be.kdg.player.adapter.in.listener;

import be.kdg.common.events.unified.MatchStatisticsEvent;
import be.kdg.player.port.in.PlayerStatisticsProjector;
import be.kdg.player.port.in.command.UpdatePlayerStatisticsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PlayerStatisticsEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PlayerStatisticsEventListener.class);
    private final PlayerStatisticsProjector projector;

    public PlayerStatisticsEventListener(PlayerStatisticsProjector projector) {
        this.projector = projector;
    }

    @EventListener(MatchStatisticsEvent.class)
    public void onMatchStatistics(MatchStatisticsEvent event) {
        logger.info("Updating player statistics for match: {}", event.matchId());

        event.playerIds().forEach(playerId ->
                projector.project(new UpdatePlayerStatisticsCommand(
                        playerId,
                        event.gameId(),
                        event.winnerId(),
                        event.startedAt(),
                        event.finishedAt()
                ))
        );
    }
}