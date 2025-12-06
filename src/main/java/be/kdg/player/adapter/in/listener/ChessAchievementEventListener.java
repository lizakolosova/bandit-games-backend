package be.kdg.player.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.chess.AchievementAcquiredEvent;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.AchievementProjection;
import be.kdg.player.port.in.AchievementProjectionProjector;
import be.kdg.player.port.in.AchievementProjector;
import be.kdg.player.port.in.command.ChessAchievementUnlockedProjectionCommand;
import be.kdg.player.port.out.LoadAchievementProjectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChessAchievementEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChessAchievementEventListener.class);

    private final AchievementProjector projector;
    private final LoadAchievementProjectionPort loadAchievementProjectionPort;

    public ChessAchievementEventListener(AchievementProjector projector,
                                         LoadAchievementProjectionPort loadAchievementProjectionPort) {
        this.projector = projector;
        this.loadAchievementProjectionPort = loadAchievementProjectionPort;
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_ACHIEVEMENT_QUEUE)
    public void onAchievementUnlocked(AchievementAcquiredEvent event) {
        logger.info("Chess achievement unlocked event received: {}", event);

        AchievementProjection projection = loadAchievementProjectionPort.loadByGameIdAndType(GameId.of(UUID.fromString(event.gameId())),
                event.achievementType()).orElseThrow(() -> new IllegalStateException("Achievement not found: gameId=" + event.gameId() +
                        " type=" + event.achievementType()));

        projector.project(new ChessAchievementUnlockedProjectionCommand(UUID.fromString(event.playerId()),
                projection.getAchievementId().uuid(), UUID.fromString(event.gameId()), event.achievementType(),
                event.timestamp()));
    }
}