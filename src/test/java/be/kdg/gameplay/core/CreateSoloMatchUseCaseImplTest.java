package be.kdg.gameplay.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.AIPlayer;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.CreateSoloMatchCommand;
import be.kdg.gameplay.port.out.AddMatchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSoloMatchUseCaseImplTest {

    @Mock
    private AddMatchPort addMatchPort;

    @InjectMocks
    private CreateSoloMatchUseCaseImpl useCase;

    @Test
    void shouldCreateSoloMatchStartItAndPersistIt() {
        // arrange
        UUID playerId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();

        CreateSoloMatchCommand command = new CreateSoloMatchCommand(playerId, gameId);

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);

        // act
        Match result = useCase.createSoloMatch(command);

        // assert
        verify(addMatchPort).add(matchCaptor.capture());
        Match saved = matchCaptor.getValue();
        assertNotNull(saved);

        assertSame(saved, result);

        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(PlayerId.of(playerId)));
        assertTrue(result.getPlayers().contains(AIPlayer.AI_PLAYER));

        assertEquals(new GameId(gameId), result.getGameId());
        assertEquals(MatchStatus.IN_PROGRESS, result.getStatus());
    }
}