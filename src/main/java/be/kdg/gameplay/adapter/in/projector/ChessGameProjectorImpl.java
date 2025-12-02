package be.kdg.gameplay.adapter.in.projector;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.port.in.command.ChessGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.ChessGameProjector;
import be.kdg.gameplay.port.in.command.ChessGameUpdatedProjectionCommand;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadMatchPort;
import be.kdg.gameplay.port.out.UpdateMatchPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChessGameProjectorImpl implements ChessGameProjector {

    private final LoadMatchPort loadMatchPort;
    private final UpdateMatchPort updateMatchPort;
    private final AddMatchPort addMatchPort;

    public ChessGameProjectorImpl(LoadMatchPort loadMatchPort, UpdateMatchPort updateMatchPort, AddMatchPort addMatchPort) {
        this.loadMatchPort = loadMatchPort;
        this.updateMatchPort = updateMatchPort;
        this.addMatchPort = addMatchPort;
    }

//    @Override
//    public void project(ChessGameCreatedProjectionCommand command) {
//        Match match = Match.begin(MatchId.of(UUID.fromString(command.gameId())), List.of(
//                PlayerId.of(UUID.fromString(command.whitePlayer())), PlayerId.of(UUID.fromString(command.blackPlayer()))),
//                command.timestamp());
//        addMatchPort.add(match);
//    }

    @Override
    public void project(ChessGameUpdatedProjectionCommand command) {
        Match match = loadMatchPort.loadById(MatchId.of(UUID.fromString(command.gameId())));

        match.updatePlayers(PlayerId.of((UUID.fromString(command.whitePlayer()))),
                PlayerId.of((UUID.fromString(command.blackPlayer()))));

        updateMatchPort.update(match);
    }

    @Override
    public void project(ChessGameEndedProjectionCommand command) {
        Match match = loadMatchPort.loadById((MatchId.of((UUID.fromString(command.gameId())))));

        PlayerId winner = resolveWinner(match, command.winner());

        match.finish(command.occurredAt(), winner);
        updateMatchPort.update(match);
    }
    private PlayerId resolveWinner(Match match, String winner) {
        return switch (winner.toUpperCase()) {
            case "WHITE" -> match.getPlayers().get(0);
            case "BLACK" -> match.getPlayers().get(1);
            default -> null; // DRAW
        };
    }
}