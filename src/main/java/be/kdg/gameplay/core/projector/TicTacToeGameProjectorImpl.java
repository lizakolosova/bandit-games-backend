package be.kdg.gameplay.core.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.TicTacToeGameProjector;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadMatchPort;
import be.kdg.gameplay.port.out.UpdateMatchPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TicTacToeGameProjectorImpl implements TicTacToeGameProjector {

    private static final Logger logger = LoggerFactory.getLogger(TicTacToeGameProjectorImpl.class);

    private final AddMatchPort addMatchPort;
    private final UpdateMatchPort updateMatchPort;
    private final LoadMatchPort loadMatchPort;

    public TicTacToeGameProjectorImpl(AddMatchPort addMatchPort, UpdateMatchPort updateMatchPort, LoadMatchPort loadMatchPort) {
        this.addMatchPort = addMatchPort;
        this.updateMatchPort = updateMatchPort;
        this.loadMatchPort = loadMatchPort;
    }

    @Override
    public void project(TicTacToeGameCreatedProjectionCommand command) {
        logger.info("Projecting TicTacToe game created: {}", command);

        Match match = new Match(MatchId.of(UUID.fromString(command.matchId())), GameId.of(UUID.fromString(command.gameId())),
                List.of(PlayerId.of(UUID.fromString(command.hostPlayerId())),  PlayerId.of(UUID.fromString(command.opponentPlayerId()))),
                MatchStatus.STARTED, command.timestamp());

        addMatchPort.add(match);
    }

    @Override
    public void project(TicTacToeGameUpdatedProjectionCommand command) {
        logger.info("Projecting TicTacToe game updated: {}", command);

//        Match match = loadMatchPort.loadById(MatchId.of(UUID.fromString(command.matchId())));
        // maybe we need some logic here if we decide to save some data for the game
    }

    @Override
    public void project(TicTacToeGameEndedProjectionCommand command) {
        logger.info("Projecting TicTacToe game ended: {}", command);

        Match match = loadMatchPort.loadById(MatchId.of(UUID.fromString(command.matchId())));

        match.finish(command.timestamp(), command.winnerId() == null ? null : PlayerId.of(UUID.fromString(command.winnerId())));
        updateMatchPort.update(match);
    }
}