package be.kdg.platform.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.command.RegisterChessGameProjectionCommand;
import be.kdg.platform.port.in.ChessPlatformProjector;
import be.kdg.platform.port.out.AddGamePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ChessPlatformProjectorImpl implements ChessPlatformProjector {

    private final AddGamePort addGamePort;
    private final PlatformEventPublisher eventPublisher;

    public ChessPlatformProjectorImpl(AddGamePort addGamePort, PlatformEventPublisher eventPublisher) {
        this.addGamePort = addGamePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void project(RegisterChessGameProjectionCommand command) {

        Game game = new Game(
                GameId.of(UUID.fromString(command.registrationId())),
                "Chess",
//                "Chess is a two-player strategy game where the goal is to checkmate the opponent’s king. Each player has 16 pieces: 1 king, 1 queen, 2 rooks, 2 bishops, 2 knights, and 8 pawns. Pieces move differently: the king moves one square in any direction, the queen any number of squares in any direction, rooks straight lines, bishops diagonally, knights in an L-shape, and pawns forward (capturing diagonally). Special moves include castling, en passant, and pawn promotion. The game ends with checkmate, stalemate, or a draw.",
                "Standard rules",
                command.pictureUrl(),
                command.frontendUrl(),
                "Board Game",
                "Chess Platform",
                LocalDate.now(),
                30
        );

        command.achievements().forEach(a ->
                game.addAchievement(a.code(), a.description(), a.description())
        );
        eventPublisher.publishEvents(game.pullDomainEvents());
        addGamePort.add(game);
    }
}
