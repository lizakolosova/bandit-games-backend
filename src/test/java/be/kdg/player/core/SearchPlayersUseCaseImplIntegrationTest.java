package be.kdg.player.core;

import be.kdg.TestHelper;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.config.TestContainersConfig;
import be.kdg.player.adapter.out.PlayerJpaRepository;
import be.kdg.player.adapter.out.mapper.PlayerJpaMapper;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.SearchPlayersCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class SearchPlayersUseCaseImplIntegrationTest {

    @Autowired
    private SearchPlayersUseCaseImpl sut;

    @Autowired
    private PlayerJpaRepository playersRepo;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldReturnAllPlayersExceptLoggedIn() {
        // Arrange
        UUID loggedInId = UUID.randomUUID();

        Player loggedIn = new Player(PlayerId.of(loggedInId), "me", "me@mail.com", "me.png", LocalDateTime.now());
        Player p1 = new Player(PlayerId.create(), "alice", "alice@mail.com", "pic1.png", LocalDateTime.now());
        Player p2 = new Player(PlayerId.create(), "bob", "bob@mail.com", "pic2.png", LocalDateTime.now());

        playersRepo.saveAll(List.of(
                PlayerJpaMapper.toEntity(loggedIn),
                PlayerJpaMapper.toEntity(p1),
                PlayerJpaMapper.toEntity(p2)
        ));

        var command = new SearchPlayersCommand(loggedInId, null);

        // Act
        List<Player> result = sut.search(command);

        // Assert
        assertEquals(2, result.size());
        assertFalse(result.stream().anyMatch(p -> p.getPlayerId().uuid().equals(loggedInId)));
    }

    @Test
    void shouldSearchPlayersByName() {
        // Arrange
        UUID loggedInId = UUID.randomUUID();

        Player loggedIn = new Player(PlayerId.of(loggedInId), "me", "me@mail.com", "me.png", LocalDateTime.now());
        Player p1 = new Player(PlayerId.create(), "Charlie", "c@mail.com", "pic1.png", LocalDateTime.now());
        Player p2 = new Player(PlayerId.create(), "Charlotte", "ch@mail.com", "pic2.png", LocalDateTime.now());
        Player p3 = new Player(PlayerId.create(), "David", "d@mail.com", "pic3.png", LocalDateTime.now());

        playersRepo.saveAll(List.of(
                PlayerJpaMapper.toEntity(loggedIn),
                PlayerJpaMapper.toEntity(p1),
                PlayerJpaMapper.toEntity(p2),
                PlayerJpaMapper.toEntity(p3)
        ));

        var command = new SearchPlayersCommand(loggedInId, "char");

        // Act
        List<Player> result = sut.search(command);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p ->
                p.getUsername().toLowerCase().contains("char")
        ));
    }

    @Test
    void shouldReturnEmptyListWhenNoPlayersMatch() {
        // Arrange
        UUID loggedInId = UUID.randomUUID();
        Player loggedIn = new Player(PlayerId.of(loggedInId), "me", "me@mail.com", "me.png", LocalDateTime.now());
        playersRepo.save(PlayerJpaMapper.toEntity(loggedIn));

        var command = new SearchPlayersCommand(loggedInId, "zzz");

        // Act
        List<Player> result = sut.search(command);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldIgnoreCaseWhenSearching() {
        // Arrange
        UUID loggedInId = UUID.randomUUID();

        Player loggedIn = new Player(PlayerId.of(loggedInId), "me", "me@mail.com", "me.png", LocalDateTime.now());
        Player p1 = new Player(PlayerId.create(), "ALICE", "alice@mail.com", "pic1.png", LocalDateTime.now());

        playersRepo.saveAll(List.of(
                PlayerJpaMapper.toEntity(loggedIn),
                PlayerJpaMapper.toEntity(p1)
        ));

        var command = new SearchPlayersCommand(loggedInId, "ali");

        // Act
        List<Player> result = sut.search(command);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ALICE", result.get(0).getUsername());
    }
}
