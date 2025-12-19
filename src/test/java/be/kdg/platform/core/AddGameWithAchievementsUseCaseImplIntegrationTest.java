package be.kdg.platform.core;

import static org.junit.jupiter.api.Assertions.*;

import be.kdg.TestHelper;
import be.kdg.config.TestContainersConfig;
import be.kdg.platform.adapter.out.AchievementDefinitionJpaRepository;
import be.kdg.platform.adapter.out.GameJpaRepository;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.command.AddGameWithAchievementsCommand;
import be.kdg.platform.port.in.command.AchievementDefinitionCommand;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class AddGameWithAchievementsUseCaseImplIntegrationTest {

    @Autowired
    private AddGameWithAchievementsUseCaseImpl sut;

    @Autowired
    private GameJpaRepository games;

    @Autowired
    private AchievementDefinitionJpaRepository achievements;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldCreateGameWithAchievements() {
        // Arrange
        var command = new AddGameWithAchievementsCommand(
                "Space Shooter",
                "Shoot enemies",
                "pic.png",
                "http://game-url",
                "Arcade",
                "Indie Developer",
                LocalDate.of(2024, 1, 1),
                12,
                List.of(
                        new AchievementDefinitionCommand(
                                "First Kill",
                                "Kill your first enemy"
                        ),
                        new AchievementDefinitionCommand(
                                "Boss Defeated",
                                "Defeat the level 1 boss"
                        )
                )
        );

        // Act
        Game game = sut.create(command);

        // Assert
        assertNotNull(game);
        assertEquals("Space Shooter", game.getName());
        assertEquals(2, game.getAchievements().size());

        var saved = games.findById(game.getGameId().uuid()).orElseThrow();
        assertEquals("Space Shooter", saved.getName());

        var achievementsInDb = achievements.findAll();
        assertEquals(2, achievementsInDb.size());

        assertTrue(
                achievementsInDb.stream()
                        .anyMatch(a -> a.getName().equals("First Kill"))
        );
        assertTrue(
                achievementsInDb.stream()
                        .anyMatch(a -> a.getName().equals("Boss Defeated"))
        );
    }

    @Test
    void shouldCreateGameWithoutAchievements() {
        // Arrange
        var command = new AddGameWithAchievementsCommand(
                "Puzzle Master",
                "Solve puzzles",
                "pic.png",
                "http://game-url-puzzle",
                "Puzzle",
                "Some Studio",
                LocalDate.of(2024, 2, 1),
                20,
                List.of()
        );

        // Act
        Game created = sut.create(command);

        // Assert
        assertNotNull(created);
        assertEquals(0, created.getAchievements().size());

        var saved = games.findById(created.getGameId().uuid()).orElseThrow();
        assertEquals("Puzzle Master", saved.getName());
        assertTrue(achievements.findAll().isEmpty());
    }

    @Test
    void shouldHandleNullAchievementsList() {
        // Arrange
        var command = new AddGameWithAchievementsCommand(
                "Adventure Land",
                "Explore map",
                "img.png",
                "http://adv",
                "Adventure",
                "AAA Studio",
                LocalDate.of(2024, 3, 1),
                50,
                null // null list
        );

        // Act
        Game created = sut.create(command);

        // Assert
        assertNotNull(created);
        assertEquals(0, created.getAchievements().size());

        assertTrue(achievements.findAll().isEmpty());
    }

}
