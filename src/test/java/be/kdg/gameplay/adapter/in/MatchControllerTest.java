package be.kdg.gameplay.adapter.in;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.RetrieveLatestMatchUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetrieveLatestMatchUseCase retrieveLatestMatchUseCase;

    @Test
    @WithMockUser(username = "test-user", roles = {"user"})
    void shouldReturnLatestMatchForPlayer() throws Exception {
        // Arrange
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        Mockito.when(retrieveLatestMatchUseCase.LoadLatestMatchByPlayer(any()))
                .thenReturn(new Match(MatchId.of(matchId), GameId.of(gameId), List.of(PlayerId.of(player1), PlayerId.of(player2)), MatchStatus.IN_PROGRESS, now()));

        // Act + Assert
        mockMvc.perform(get("/api/matches/" + player1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("\"" + matchId + "\""));
    }


    @Test
    @WithMockUser(username = "test-user", roles = {"user"})
    void shouldReturn400ForInvalidUUID() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/api/matches/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturn401WhenUnauthorized() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/api/matches/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }
}