package be.kdg.gameplay.core;

import be.kdg.TestHelper;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.config.TestContainersConfig;
import be.kdg.gameplay.adapter.out.MatchJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class ShowMatchHistoryUseCaseImplIntegrationTest {
    @Autowired
    private ShowMatchHistoryUseCaseImpl showMatchHistoryUseCase;

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Autowired
    private TestHelper testHelper;

    @Test
    void returnsEmptyListWhenPlayerHasNoMatches() {
        var playerId = PlayerId.of(UUID.randomUUID());

        var result = showMatchHistoryUseCase.showHistory(playerId);

        assertThat(result).isEmpty();
    }

    @Test
    void returnsOnlyMatchesForThatPlayer() {
        var player = UUID.randomUUID();
        var other = UUID.randomUUID();

        matchJpaRepository.save(testHelper.buildMatchEntityForPlayer(player));
        matchJpaRepository.save(testHelper.buildMatchEntityForPlayer(player));
        matchJpaRepository.save(testHelper.buildMatchEntityForPlayer(other));
        var result = showMatchHistoryUseCase.showHistory(PlayerId.of(player));

        assertThat(result).hasSize(2);
    }

    @Test
    void throwsWhenPlayerIdIsNull() {
        assertThatThrownBy(() -> showMatchHistoryUseCase.showHistory(null))
                .isInstanceOf(NullPointerException.class);
    }
}