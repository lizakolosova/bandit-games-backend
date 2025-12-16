package be.kdg.player.adapter.in.listener;

import be.kdg.acl.translator.ChessEventTranslator;
import be.kdg.acl.translator.TicTacToeEventTranslator;
import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.chess.AchievementAcquiredEvent;
import be.kdg.common.events.tictactoe.TicTacToeAchievementAchievedEvent;
import be.kdg.common.events.unified.UnifiedAchievementAchievedEvent;
import be.kdg.player.port.in.UnifiedAchievementProjector;
import be.kdg.player.port.in.command.UnifiedAchievementUnlockedProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UnifiedAchievementEventListener {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedAchievementEventListener.class);

    private final UnifiedAchievementProjector projector;
    private final ChessEventTranslator chessTranslator;
    private final TicTacToeEventTranslator tttTranslator;

    public UnifiedAchievementEventListener(UnifiedAchievementProjector projector,
                                           ChessEventTranslator chessTranslator,
                                           TicTacToeEventTranslator tttTranslator) {
        this.projector = projector;
        this.chessTranslator = chessTranslator;
        this.tttTranslator = tttTranslator;
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_ACHIEVEMENT_QUEUE, containerFactory = "externalRabbitListenerContainerFactory")
    public void onChessAchievementUnlocked(AchievementAcquiredEvent event) {
        logger.info("Chess achievement unlocked event received: {}", event);

        UnifiedAchievementAchievedEvent unifiedEvent = chessTranslator.translateToAchievementAchieved(event);

        projector.projectAchievementUnlocked(new UnifiedAchievementUnlockedProjectionCommand(
                unifiedEvent.playerId(),
                unifiedEvent.achievementId(),
                unifiedEvent.matchId(),
                unifiedEvent.achievementType(),
                unifiedEvent.gameType(),
                unifiedEvent.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_ACHIEVEMENT_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void onTicTacToeAchievementUnlocked(TicTacToeAchievementAchievedEvent event) {
        logger.info("TicTacToe achievement unlocked event received: {}", event);

        UnifiedAchievementAchievedEvent unifiedEvent = tttTranslator.translateToAchievementAchieved(event);

        projector.projectAchievementUnlocked(new UnifiedAchievementUnlockedProjectionCommand(
                unifiedEvent.playerId(),
                unifiedEvent.achievementId(),
                unifiedEvent.matchId(),
                unifiedEvent.achievementType(),
                unifiedEvent.gameType(),
                unifiedEvent.timestamp()
        ));
    }
}