package be.kdg.player.adapter.in.listener;

import be.kdg.common.events.unified.MatchStatisticsEvent;
import be.kdg.config.TestContainersConfig;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class PlayerStatisticsEventListenerIntegrationTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private LoadPlayerPort loadPlayerPort;

    @Autowired
    private UpdatePlayerPort updatePlayerPort;

    private Player player1;
    private Player player2;
    private UUID gameId;

    private static final UUID TTT_GAME_ID = UUID.fromString("b90a72ac-b27b-428d-b99e-51a8c2abfccb");

    @BeforeEach
    void setUp() {
        gameId = TTT_GAME_ID;

        player1 = new Player("Player1", "player1@test.com", "pic1.jpg");
        player2 = new Player("Player2", "player2@test.com", "pic2.jpg");
        player1.addGameToLibrary(TTT_GAME_ID);
        player2.addGameToLibrary(TTT_GAME_ID);

        updatePlayerPort.update(player1);
        updatePlayerPort.update(player2);
    }

    @Test
    void shouldUpdateStatisticsForBothPlayersWhenMatchEnds() {
        // Arrange
        LocalDateTime startedAt = LocalDateTime.now().minusMinutes(30);
        LocalDateTime finishedAt = LocalDateTime.now();

        MatchStatisticsEvent event = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid(), player2.getPlayerId().uuid()),
                player1.getPlayerId().uuid(),
                startedAt,
                finishedAt,
                "CHECKMATE",
                LocalDateTime.now()
        );
        // Act
        eventPublisher.publishEvent(event);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            Player updatedPlayer1 = loadPlayerPort.loadById(player1.getPlayerId()).orElseThrow();
            Player updatedPlayer2 = loadPlayerPort.loadById(player2.getPlayerId()).orElseThrow();

            GameLibrary library1 = updatedPlayer1.findGameInLibrary(gameId);
            GameLibrary library2 = updatedPlayer2.findGameInLibrary(gameId);

            // Assert
            assertThat(library1).isNotNull();
            assertThat(library2).isNotNull();

            // Player 1 won
            assertThat(library1.getMatchesPlayed()).isEqualTo(1);
            assertThat(library1.getGamesWon()).isEqualTo(1);
            assertThat(library1.getGamesLost()).isEqualTo(0);
            assertThat(library1.getGamesDraw()).isEqualTo(0);
            assertThat(library1.getLastPlayedAt()).isNotNull();

            // Player 2 lost
            assertThat(library2.getMatchesPlayed()).isEqualTo(1);
            assertThat(library2.getGamesWon()).isEqualTo(0);
            assertThat(library2.getGamesLost()).isEqualTo(1);
            assertThat(library2.getGamesDraw()).isEqualTo(0);
            assertThat(library2.getLastPlayedAt()).isNotNull();
        });
    }

    @Test
    void shouldRecordDrawWhenNoWinner() {
        // Arrange
        LocalDateTime startedAt = LocalDateTime.now().minusMinutes(15);
        LocalDateTime finishedAt = LocalDateTime.now();

        MatchStatisticsEvent event = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid(), player2.getPlayerId().uuid()),
                null,
                startedAt,
                finishedAt,
                "STALEMATE",
                LocalDateTime.now()
        );

        // Act
        eventPublisher.publishEvent(event);

        // Assert
        await().atMost(5, SECONDS).untilAsserted(() -> {
            Player updatedPlayer1 = loadPlayerPort.loadById(player1.getPlayerId()).orElseThrow();
            Player updatedPlayer2 = loadPlayerPort.loadById(player2.getPlayerId()).orElseThrow();

            GameLibrary library1 = updatedPlayer1.findGameInLibrary(gameId);
            GameLibrary library2 = updatedPlayer2.findGameInLibrary(gameId);

            // Both players should have a draw
            assertThat(library1.getMatchesPlayed()).isEqualTo(1);
            assertThat(library1.getGamesDraw()).isEqualTo(1);
            assertThat(library1.getGamesWon()).isEqualTo(0);
            assertThat(library1.getGamesLost()).isEqualTo(0);

            assertThat(library2.getMatchesPlayed()).isEqualTo(1);
            assertThat(library2.getGamesDraw()).isEqualTo(1);
            assertThat(library2.getGamesWon()).isEqualTo(0);
            assertThat(library2.getGamesLost()).isEqualTo(0);
        });
    }

    @Test
    void shouldAccumulateStatisticsAcrossMultipleMatches() {
        // Arrange
        LocalDateTime startedAt = LocalDateTime.now().minusMinutes(30);
        LocalDateTime finishedAt = LocalDateTime.now();

        MatchStatisticsEvent event1 = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid(), player2.getPlayerId().uuid()),
                player1.getPlayerId().uuid(),
                startedAt,
                finishedAt,
                "CHECKMATE",
                LocalDateTime.now()
        );

        MatchStatisticsEvent event2 = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid(), player2.getPlayerId().uuid()),
                player2.getPlayerId().uuid(),
                startedAt.plusMinutes(45),
                finishedAt.plusMinutes(45),
                "TIMEOUT",
                LocalDateTime.now()
        );

        MatchStatisticsEvent event3 = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid(), player2.getPlayerId().uuid()),
                null,
                startedAt.plusMinutes(90),
                finishedAt.plusMinutes(90),
                "STALEMATE",
                LocalDateTime.now()
        );

        // Act
        eventPublisher.publishEvent(event1);
        eventPublisher.publishEvent(event2);
        eventPublisher.publishEvent(event3);

        // Assert
        await().atMost(5, SECONDS).untilAsserted(() -> {
            Player updatedPlayer1 = loadPlayerPort.loadById(player1.getPlayerId()).orElseThrow();
            Player updatedPlayer2 = loadPlayerPort.loadById(player2.getPlayerId()).orElseThrow();

            GameLibrary library1 = updatedPlayer1.findGameInLibrary(gameId);
            GameLibrary library2 = updatedPlayer2.findGameInLibrary(gameId);

            // Player 1: 1 win, 1 loss, 1 draw
            assertThat(library1.getMatchesPlayed()).isEqualTo(3);
            assertThat(library1.getGamesWon()).isEqualTo(1);
            assertThat(library1.getGamesLost()).isEqualTo(1);
            assertThat(library1.getGamesDraw()).isEqualTo(1);

            // Player 2: 1 win, 1 loss, 1 draw
            assertThat(library2.getMatchesPlayed()).isEqualTo(3);
            assertThat(library2.getGamesWon()).isEqualTo(1);
            assertThat(library2.getGamesLost()).isEqualTo(1);
            assertThat(library2.getGamesDraw()).isEqualTo(1);
        });
    }

    @Test
    void shouldCalculatePlaytimeWhenStartAndEndTimesProvided() {
        // Arrange
        LocalDateTime startedAt = LocalDateTime.now().minusMinutes(30);
        LocalDateTime finishedAt = LocalDateTime.now();

        MatchStatisticsEvent event = new MatchStatisticsEvent(
                UUID.randomUUID(),
                gameId,
                List.of(player1.getPlayerId().uuid()),
                player1.getPlayerId().uuid(),
                startedAt,
                finishedAt,
                "CHECKMATE",
                LocalDateTime.now()
        );

        // Act
        eventPublisher.publishEvent(event);

        // Assert
        await().atMost(5, SECONDS).untilAsserted(() -> {
            Player updatedPlayer = loadPlayerPort.loadById(player1.getPlayerId()).orElseThrow();
            GameLibrary library = updatedPlayer.findGameInLibrary(gameId);

            assertThat(library.getTotalPlaytime()).isNotNull();
            assertThat(library.getTotalPlaytime().toMinutes()).isGreaterThanOrEqualTo(29);
            assertThat(library.getTotalPlaytime().toMinutes()).isLessThanOrEqualTo(31);
        });
    }
}