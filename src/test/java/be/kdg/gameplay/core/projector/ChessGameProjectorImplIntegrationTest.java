package be.kdg.gameplay.core.projector;

import static org.junit.jupiter.api.Assertions.*;

import be.kdg.TestHelper;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.config.TestContainersConfig;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class ChessGameProjectorImplIntegrationTest {

    @Autowired
    private ChessGameProjectorImpl sut;

    @Autowired
    private AddMatchPort addMatchPort;
    @Autowired
    private LoadMatchPort loadMatchPort;
    @Autowired
    private UpdateMatchPort updateMatchPort;

    @Autowired
    private AddGameViewProjectionPort addGameViewProjectionPort;
    @Autowired
    private LoadGameViewProjectionPort loadGameViewProjectionPort;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void cleanup() {
        testHelper.cleanUp();
    }

    @Test
    void shouldCreateMatchWhenGameCreatedEventArrives() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        UUID whiteId = UUID.randomUUID();
        UUID blackId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        GameViewProjection projection = new GameViewProjection(
                GameId.of(gameId),
                "Chess"
        );
        addGameViewProjectionPort.add(projection);

        var command = new ChessGameCreatedProjectionCommand(
                matchId,
                "Chess",
                whiteId,
                blackId,
                ts
        );

        // Act
        sut.project(command);

        // Assert
        Match saved = loadMatchPort.loadById(MatchId.of(matchId));
        assertNotNull(saved);
        assertEquals(2, saved.getPlayers().size());
        assertEquals(PlayerId.of(whiteId), saved.getPlayers().get(0));
        assertEquals(PlayerId.of(blackId), saved.getPlayers().get(1));
        assertEquals(MatchStatus.IN_PROGRESS, saved.getStatus());
    }

    @Test
    void shouldUpdatePlayersOnGameUpdatedEvent() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID whiteId = UUID.randomUUID();
        UUID blackId = UUID.randomUUID();

        Match match = new Match(
                MatchId.of(gameId),
                GameId.of(UUID.randomUUID()),
                List.of(PlayerId.of(whiteId), PlayerId.of(blackId)),
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now()
        );
        addMatchPort.add(match);

        UUID newWhite = UUID.randomUUID();
        UUID newBlack = UUID.randomUUID();

        var command = new ChessGameUpdatedProjectionCommand(
                gameId,
                newWhite,
                newBlack,
                LocalDateTime.now()
        );

        // Act
        sut.project(command);

        // Assert
        Match updated = loadMatchPort.loadById(MatchId.of(gameId));
        assertEquals(PlayerId.of(newWhite), updated.getPlayers().get(0));
        assertEquals(PlayerId.of(newBlack), updated.getPlayers().get(1));
    }

    @Test
    void shouldFinishMatchWhenGameEndsAndDetermineWinner() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID white = UUID.randomUUID();
        UUID black = UUID.randomUUID();

        Match match = new Match(
                MatchId.of(matchId),
                GameId.of(UUID.randomUUID()),
                List.of(PlayerId.of(white), PlayerId.of(black)),
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now()
        );
        addMatchPort.add(match);

        LocalDateTime endedAt = LocalDateTime.now();

        var command = new ChessGameEndedProjectionCommand(
                matchId,
                white,
                black,
                "WHITE",
                "CHECKMATE",
                42,
                endedAt
        );

        // Act
        sut.project(command);

        // Assert
        Match updated = loadMatchPort.loadById(MatchId.of(matchId));

        assertEquals(MatchStatus.FINISHED, updated.getStatus());
        assertEquals(PlayerId.of(white), updated.getWinnerPlayerId());
    }


    @Test
    void shouldHandleBlackWinnerCorrectly() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID white = UUID.randomUUID();
        UUID black = UUID.randomUUID();

        Match match = new Match(
                MatchId.of(matchId),
                GameId.of(UUID.randomUUID()),
                List.of(PlayerId.of(white), PlayerId.of(black)),
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now()
        );
        addMatchPort.add(match);

        var command = new ChessGameEndedProjectionCommand(
                matchId,
                white,
                black,
                "BLACK",
                "CHECKMATE",
                12,
                LocalDateTime.now()
        );

        // Act
        sut.project(command);

        // Assert
        Match updated = loadMatchPort.loadById(MatchId.of(matchId));
        assertEquals(PlayerId.of(black), updated.getWinnerPlayerId());
    }

    @Test
    void shouldFailToCreateMatchIfGameProjectionDoesNotExist() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();

        var cmd = new ChessGameCreatedProjectionCommand(
                matchId,
                "Good game",
                p1,
                p2,
                LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(
                Exception.class,
                () -> sut.project(cmd),
                "Creating a match without a GameViewProjection should fail"
        );
    }

    @Test
    void shouldFailToUpdateMatchIfMatchDoesNotExist() {
        // Arrange
        UUID nonexistent = UUID.randomUUID();

        var cmd = new ChessGameUpdatedProjectionCommand(
                nonexistent,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(
                Exception.class,
                () -> sut.project(cmd),
                "Updating a non-existing match must fail"
        );
    }
    @Test
    void shouldFailToEndMatchIfMatchDoesNotExist() {
        // Arrange
        UUID nonexistent = UUID.randomUUID();

        var cmd = new ChessGameEndedProjectionCommand(
                nonexistent,
                UUID.randomUUID(),
                UUID.randomUUID(),
                "WHITE",
                "CHECKMATE",
                10,
                LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(
                Exception.class,
                () -> sut.project(cmd),
                "Ending a non-existing match must fail"
        );
    }
    @Test
    void shouldSetWinnerToNullWhenWinnerStringIsInvalid() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID white = UUID.randomUUID();
        UUID black = UUID.randomUUID();

        Match match = new Match(
                MatchId.of(matchId),
                GameId.of(UUID.randomUUID()),
                List.of(PlayerId.of(white), PlayerId.of(black)),
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now()
        );
        addMatchPort.add(match);

        var cmd = new ChessGameEndedProjectionCommand(
                matchId,
                white,
                black,
                "BANANA",       // invalid
                "CHECKMATE",
                42,
                LocalDateTime.now()
        );

        // Act
        sut.project(cmd);

        // Assert
        Match updated = loadMatchPort.loadById(MatchId.of(matchId));
        assertNull(updated.getWinnerPlayerId(), "Winner should be null for invalid winner string");
        assertEquals(MatchStatus.FINISHED, updated.getStatus());
    }
    @Test
    void shouldFailWhenWinnerIsNull() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID white = UUID.randomUUID();
        UUID black = UUID.randomUUID();

        Match match = new Match(
                MatchId.of(matchId),
                GameId.of(UUID.randomUUID()),
                List.of(PlayerId.of(white), PlayerId.of(black)),
                MatchStatus.IN_PROGRESS,
                LocalDateTime.now()
        );
        addMatchPort.add(match);

        var cmd = new ChessGameEndedProjectionCommand(
                matchId,
                white,
                black,
                null,
                "CHECKMATE",
                20,
                LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> sut.project(cmd),
                "Null winner should cause failure"
        );
    }
}
