package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.tictactoe.TicTacToeMatchCreatedEvent;
import be.kdg.common.events.tictactoe.TicTacToeMatchUpdatedEvent;
import be.kdg.common.events.tictactoe.TicTacToeMatchEndedEvent;
import be.kdg.gameplay.port.in.TicTacToeGameProjector;
import be.kdg.gameplay.port.in.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeGameplayEventListener {

    private final TicTacToeGameProjector projector;
    private static final Logger logger = LoggerFactory.getLogger(TicTacToeGameplayEventListener.class);

    public TicTacToeGameplayEventListener(TicTacToeGameProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_GAME_STARTED_QUEUE)
    public void onGameCreated(TicTacToeMatchCreatedEvent event) {
        logger.info("TicTacToe game created: {}", event);
        projector.project(new TicTacToeGameCreatedProjectionCommand(
                "b90a72ac-b27b-428d-b99e-51a8c2abfccb",
                event.matchId(),
                event.hostPlayerId(),
                event.opponentPlayerId(),
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_MOVE_MADE_QUEUE)
    public void onGameUpdated(TicTacToeMatchUpdatedEvent event) {
        logger.info("TicTacToe game updated: {}", event);
        projector.project(new TicTacToeGameUpdatedProjectionCommand(
                event.matchId(),
                event.playerId(),
                event.moveNumber(),
                event.position(),
                event.boardStateAfterMove(),
                event.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_GAME_ENDED_QUEUE)
    public void onGameEnded(TicTacToeMatchEndedEvent event) {
        logger.info("TicTacToe game ended: {}", event);
        projector.project(new TicTacToeGameEndedProjectionCommand(
                event.matchId(),
                event.winnerId(),
                event.endReason(),
                event.totalMoves(),
                event.timestamp()
        ));
    }
}