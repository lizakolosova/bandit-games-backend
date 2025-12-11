package be.kdg.gameplay.adapter.in.listener;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.chess.*;
import be.kdg.common.events.tictactoe.*;
import be.kdg.acl.translator.ChessEventTranslator;
import be.kdg.acl.translator.TicTacToeEventTranslator;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.adapter.out.GameRoomStatusBroadcaster;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.port.in.UnifiedMatchProjector;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.LoadGameViewProjectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UnifiedGameplayEventListener {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedGameplayEventListener.class);
    private static final UUID TTT_GAME_ID = UUID.fromString("b90a72ac-b27b-428d-b99e-51a8c2abfccb");

    private final UnifiedMatchProjector projector;
    private final ChessEventTranslator chessTranslator;
    private final TicTacToeEventTranslator tttTranslator;
    private final LoadGameViewProjectionPort loadGameViewProjectionPort;
    private final GameRoomStatusBroadcaster broadcaster;
    private final LoadGameRoomPort loadGameRoomPort;

    public UnifiedGameplayEventListener(UnifiedMatchProjector projector, ChessEventTranslator chessTranslator,
                                        TicTacToeEventTranslator tttTranslator, LoadGameViewProjectionPort loadGameViewProjectionPort,
                                        GameRoomStatusBroadcaster broadcaster, LoadGameRoomPort loadGameRoomPort) {
        this.projector = projector;
        this.chessTranslator = chessTranslator;
        this.tttTranslator = tttTranslator;
        this.loadGameViewProjectionPort = loadGameViewProjectionPort;
        this.broadcaster = broadcaster;
        this.loadGameRoomPort = loadGameRoomPort;
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_CREATED_QUEUE, containerFactory = "externalRabbitListenerContainerFactory")
    public void onChessMatchCreated(ChessMatchCreatedEvent event) {
        logger.info("Chess match created: {}", event.gameId());
        GameViewProjection game = loadGameViewProjectionPort.findByName("CHESS");
        logger.info("Chess id: {}", game.getGameId());
        var unifiedEvent = chessTranslator.translateToMatchCreated(event, game.getGameId());
        logger.info("event chess id: {}", unifiedEvent.gameId());
        projector.project(new UnifiedMatchCreatedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.gameId(),
                unifiedEvent.gameType(),
                unifiedEvent.playerIds(),
                unifiedEvent.timestamp()
        ));
        GameRoom gameRoom = loadGameRoomPort.findByPlayers(event.whitePlayerId(), event.blackPlayerId());
        broadcaster.broadcastMatchStarted(gameRoom.getGameRoomId().uuid().toString(), event.gameId().toString()
        );
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_UPDATED_QUEUE, containerFactory = "externalRabbitListenerContainerFactory")
    public void onChessMatchUpdated(ChessMatchUpdatedEvent event) {
        logger.info("Chess match updated: {}", event.gameId());
        var unifiedEvent = chessTranslator.translateToMatchUpdated(event);
        projector.project(new UnifiedMatchUpdatedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.player1(),
                unifiedEvent.player2(),
                unifiedEvent.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.CHESS_GAME_ENDED_QUEUE, containerFactory = "externalRabbitListenerContainerFactory")
    public void onChessMatchEnded(ChessMatchEndedEvent event) {
        logger.info("Chess match ended: {}", event.gameId());
        var unifiedEvent = chessTranslator.translateToMatchEnded(event);
        projector.project(new UnifiedMatchEndedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.winnerId(),
                unifiedEvent.endReason(),
                unifiedEvent.totalMoves(),
                unifiedEvent.timestamp()
        ));
    }
    @RabbitListener(queues = RabbitMQTopology.TTT_GAME_STARTED_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void onTicTacToeMatchCreated(TicTacToeMatchCreatedEvent event) {
        logger.info("TicTacToe match created: {}", event.matchId());
        var unifiedEvent = tttTranslator.translateToMatchCreated(event, GameId.of(TTT_GAME_ID));
        projector.project(new UnifiedMatchCreatedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.gameId(),
                unifiedEvent.gameType(),
                unifiedEvent.playerIds(),
                unifiedEvent.timestamp()
        ));
        GameRoom gameRoom = loadGameRoomPort.findByPlayers(
                event.hostPlayerId(),
                event.opponentPlayerId()
        );
        broadcaster.broadcastMatchStarted(
                gameRoom.getGameRoomId().uuid().toString(),
                event.matchId().toString()
        );
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_MOVE_MADE_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void onTicTacToeMatchUpdated(TicTacToeMatchMoveMadeEvent event) {
        logger.info("TicTacToe match updated: {}", event.matchId());
        var unifiedEvent = tttTranslator.translateToMatchUpdated(event);
        projector.project(new UnifiedMatchUpdatedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.player1(),
                unifiedEvent.player2(),
                unifiedEvent.timestamp()
        ));
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_GAME_ENDED_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void onTicTacToeMatchEnded(TicTacToeMatchEndedEvent event) {
        logger.info("TicTacToe match ended: {}", event.matchId());
        var unifiedEvent = tttTranslator.translateToMatchEnded(event);
        projector.project(new UnifiedMatchEndedProjectionCommand(
                unifiedEvent.matchId(),
                unifiedEvent.winnerId(),
                unifiedEvent.endReason(),
                unifiedEvent.totalMoves(),
                unifiedEvent.timestamp()
        ));
    }
}
