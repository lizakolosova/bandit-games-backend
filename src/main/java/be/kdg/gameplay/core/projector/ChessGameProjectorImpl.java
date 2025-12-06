package be.kdg.gameplay.core.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.command.ChessGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.ChessGameProjector;
import be.kdg.gameplay.port.in.command.ChessGameUpdatedProjectionCommand;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import be.kdg.gameplay.port.out.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Service
public class ChessGameProjectorImpl implements ChessGameProjector {

    private final LoadMatchPort loadMatchPort;
    private final UpdateMatchPort updateMatchPort;
    private final AddMatchPort addMatchPort;
    private final LoadGameViewProjectionPort loadGameViewProjectionPort;
    private final AddGameViewProjectionPort addGameViewProjectionPort;

    private static final Logger log = LoggerFactory.getLogger(ChessGameProjectorImpl.class);

    public ChessGameProjectorImpl(LoadMatchPort loadMatchPort, UpdateMatchPort updateMatchPort, AddMatchPort addMatchPort, LoadGameViewProjectionPort loadGameViewProjectionPort, AddGameViewProjectionPort addGameViewProjectionPort) {
        this.loadMatchPort = loadMatchPort;
        this.updateMatchPort = updateMatchPort;
        this.addMatchPort = addMatchPort;
        this.loadGameViewProjectionPort = loadGameViewProjectionPort;
        this.addGameViewProjectionPort = addGameViewProjectionPort;
    }

    @Override
    public void project(ChessGameCreatedProjectionCommand command) {
        GameViewProjection game = loadGameViewProjectionPort.findByName(command.gameName());
        Match match = new Match(MatchId.of(command.matchId()), game.getGameId(),
                List.of(PlayerId.of(command.whitePlayerId()), PlayerId.of(command.blackPlayerId())),
                MatchStatus.IN_PROGRESS, command.timestamp());

        addMatchPort.add(match);
    }

    @Override
    public void project(ChessGameUpdatedProjectionCommand command) {
        Match match = loadMatchPort.loadById(MatchId.of(command.gameId()));

        match.updatePlayers(PlayerId.of((command.whitePlayerId())),
                PlayerId.of((command.blackPlayerId())));

        updateMatchPort.update(match);
    }

    @Override
    public void project(ChessGameEndedProjectionCommand command) {
        Match match = loadMatchPort.loadById((MatchId.of((command.gameId()))));

        PlayerId winner = resolveWinner(match, command.winner());

        match.finish(command.occurredAt(), winner);
        updateMatchPort.update(match);
    }
    private PlayerId resolveWinner(Match match, String winner) {
        return switch (winner.toUpperCase()) {
            case "WHITE" -> match.getPlayers().get(0);
            case "BLACK" -> match.getPlayers().get(1);
            default -> null;
        };
    }

    @Override
    @Transactional
    public void project(RegisterGameViewProjectionCommand command) {
        GameViewProjection projection = new GameViewProjection(GameId.of(command.gameId()), command.name());
        addGameViewProjectionPort.add(projection);
    }
}