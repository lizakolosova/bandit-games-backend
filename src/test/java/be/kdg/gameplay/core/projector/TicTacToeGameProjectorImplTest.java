package be.kdg.gameplay.core.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.command.TicTacToeGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.TicTacToeGameEndedProjectionCommand;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadMatchPort;
import be.kdg.gameplay.port.out.UpdateMatchPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicTacToeGameProjectorImplTest {

    @Mock
    private AddMatchPort addMatchPort;

    @Mock
    private UpdateMatchPort updateMatchPort;

    @Mock
    private LoadMatchPort loadMatchPort;

    private TicTacToeGameProjectorImpl projector;

    @BeforeEach
    void setUp() {
        projector = new TicTacToeGameProjectorImpl(addMatchPort, updateMatchPort, loadMatchPort);
    }

    @Test
    void shouldCreateMatch() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID oppId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        TicTacToeGameCreatedProjectionCommand command =
                new TicTacToeGameCreatedProjectionCommand(gameId, matchId, hostId, oppId, ts);

        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);

        // Act
        projector.project(command);

        // Assert
        verify(addMatchPort).add(captor.capture());
        Match match = captor.getValue();

        assertEquals(MatchId.of(matchId), match.getMatchId());
        assertEquals(GameId.of(gameId), match.getGameId());
        assertEquals(List.of(PlayerId.of(hostId), PlayerId.of(oppId)), match.getPlayers());
        assertEquals(MatchStatus.STARTED, match.getStatus());
        assertEquals(ts, match.getStartedAt());
    }

    @Test
    void shouldEndMatchWithWinner() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        Match match = mock(Match.class);
        when(loadMatchPort.loadById(MatchId.of(matchId))).thenReturn(match);

        TicTacToeGameEndedProjectionCommand command =
                new TicTacToeGameEndedProjectionCommand(matchId, winnerId, "WIN", 7, ts);

        // Act
        projector.project(command);

        // Assert
        verify(match).finish(ts, PlayerId.of(winnerId));
        verify(updateMatchPort).update(match);
    }

    @Test
    void shouldEndMatchWithoutWinner() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        Match match = mock(Match.class);
        when(loadMatchPort.loadById(MatchId.of(matchId))).thenReturn(match);

        TicTacToeGameEndedProjectionCommand command =
                new TicTacToeGameEndedProjectionCommand(matchId, null, "DRAW", 9, ts);

        // Act
        projector.project(command);

        // Assert
        verify(match).finish(ts, null);
        verify(updateMatchPort).update(match);
    }

    @Test
    void shouldThrowWhenEndingMatchThatDoesNotExist() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        when(loadMatchPort.loadById(MatchId.of(matchId)))
                .thenThrow(new IllegalArgumentException("Match not found"));

        TicTacToeGameEndedProjectionCommand command =
                new TicTacToeGameEndedProjectionCommand(matchId, null, "DRAW", 3, ts);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> projector.project(command));

        verify(updateMatchPort, never()).update(any());
    }

    @Test
    void shouldNotUpdateMatchWhenFinishThrowsException() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        LocalDateTime ts = LocalDateTime.now();

        Match match = mock(Match.class);
        when(loadMatchPort.loadById(MatchId.of(matchId))).thenReturn(match);
        doThrow(new IllegalStateException("Invalid state")).when(match).finish(any(), any());

        TicTacToeGameEndedProjectionCommand command =
                new TicTacToeGameEndedProjectionCommand(matchId, winnerId, "WIN", 5, ts);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> projector.project(command));

        verify(updateMatchPort, never()).update(any());
    }
}
