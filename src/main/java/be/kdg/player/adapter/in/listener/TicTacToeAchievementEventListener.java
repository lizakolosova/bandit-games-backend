package be.kdg.player.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.tictactoe.TicTacToeAchievementAchievedEvent;
import be.kdg.player.port.in.AchievementProjector;

import be.kdg.player.port.in.command.TicTacToeAchievementUnlockedProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeAchievementEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TicTacToeAchievementEventListener.class);

    private final AchievementProjector projector;

    public TicTacToeAchievementEventListener(AchievementProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_ACHIEVEMENT_QUEUE)
    public void onAchievementUnlocked(TicTacToeAchievementAchievedEvent event) {
        logger.info("Achievement unlocked event received: {}", event);

        projector.project(new TicTacToeAchievementUnlockedProjectionCommand(event.playerId(), event.achievementId(), event.gameId(),
                event.achievementType(), event.timestamp()));
    }
}

