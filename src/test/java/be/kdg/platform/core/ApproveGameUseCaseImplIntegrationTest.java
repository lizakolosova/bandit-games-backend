package be.kdg.platform.core;

import be.kdg.TestHelper;
import be.kdg.config.TestContainersConfig;
import be.kdg.platform.adapter.out.GameJpaEntity;
import be.kdg.platform.adapter.out.GameJpaRepository;
import be.kdg.platform.adapter.out.mapper.GameMapper;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.ApproveGameUseCase;
import be.kdg.platform.port.in.command.ApproveGameCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class ApproveGameUseCaseImplIntegrationTest {

    @Autowired
    private ApproveGameUseCase approveGameUseCase;

    @Autowired
    private GameJpaRepository games;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldApproveGame() throws GameNotFoundException {
        // Arrange
        Game game = new Game("Test Game", "Some rules", "http://picture", "https://game",
                "Category", "Dev", now(), 10);
        GameJpaEntity saved = games.save(GameMapper.toEntity(game));
        UUID gameId = saved.getUuid();

        // Act
        approveGameUseCase.approve(new ApproveGameCommand(gameId));

        // Assert
        GameJpaEntity result = games.findById(saved.getUuid()).orElseThrow();
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    void shouldFailWhenGameDoesNotExist() {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        // Act
        Throwable thrown = catchThrowable(() ->
                approveGameUseCase.approve(new ApproveGameCommand(unknownId))
        );

        // Assert
        assertThat(thrown).isInstanceOf(GameNotFoundException.class);
    }

    @Test
    void shouldFailWhenGameEntityIsCorrupted() {
        // Arrange
        Game corrupted = new Game("Broken", "Rules", "pic", "url", "cat", "dev",
                now(), 10);
        GameJpaEntity saved = games.save(GameMapper.toEntity(corrupted));
        UUID gameId = saved.getUuid();

        games.delete(saved);

        // Act
        Throwable thrown = catchThrowable(() ->
                approveGameUseCase.approve(new ApproveGameCommand(gameId))
        );

        // Assert
        assertThat(thrown).isInstanceOf(GameNotFoundException.class);
    }

}
