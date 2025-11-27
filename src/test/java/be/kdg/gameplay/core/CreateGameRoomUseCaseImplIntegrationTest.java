package be.kdg.gameplay.core;

import be.kdg.TestHelper;
import be.kdg.config.TestContainersConfig;
import be.kdg.gameplay.adapter.out.GameRoomJpaRepository;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.CreateGameRoomCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class CreateGameRoomUseCaseImplIntegrationTest {

    @Autowired
    private CreateGameRoomUseCaseImpl sut;

    @Autowired
    private GameRoomJpaRepository rooms;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldCreateClosedRoomWithInvitedPlayer() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        var command = new CreateGameRoomCommand(
                gameId,
                hostId,
                invitedId,
                GameRoomType.CLOSED
        );

        // Act
        GameRoom room = sut.create(command);

        // Assert
        assertNotNull(room);
        assertEquals(gameId, room.getGameId().uuid());
        assertEquals(hostId, room.getHostPlayerId().uuid());
        assertEquals(invitedId, room.getInvitedPlayerId().uuid());
        assertEquals(GameRoomType.CLOSED, room.getGameRoomType());
        assertEquals(GameRoomStatus.READY, room.getStatus());

        var saved = rooms.findById(room.getGameRoomId().uuid()).orElseThrow();
        assertEquals(hostId, saved.getHostPlayerId());
        assertEquals(invitedId, saved.getInvitedPlayerId());
        assertEquals(GameRoomType.CLOSED, saved.getGameRoomType());
        assertEquals(GameRoomStatus.READY, saved.getStatus());
    }

    @Test
    void shouldCreateClosedRoomAgainstAIWhenInvitedPlayerIsNull() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();

        var command = new CreateGameRoomCommand(
                gameId,
                hostId,
                null,
                GameRoomType.CLOSED
        );

        // Act
        GameRoom room = sut.create(command);

        // Assert
        assertNotNull(room);
        assertEquals(gameId, room.getGameId().uuid());
        assertEquals(hostId, room.getHostPlayerId().uuid());
        assertNull(room.getInvitedPlayerId());
        assertEquals(GameRoomType.CLOSED, room.getGameRoomType());
        assertEquals(GameRoomStatus.READY, room.getStatus());

        var saved = rooms.findById(room.getGameRoomId().uuid()).orElseThrow();
        assertNull(saved.getInvitedPlayerId());
        assertEquals(GameRoomStatus.READY, saved.getStatus());
    }

    @Test
    void shouldAlwaysCreateNewRoomWithNewId() {
        // Arrange
        var command = new CreateGameRoomCommand(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                GameRoomType.CLOSED
        );

        // Act
        GameRoom room1 = sut.create(command);
        GameRoom room2 = sut.create(command);

        // Assert
        assertNotEquals(room1.getGameRoomId(), room2.getGameRoomId());
        assertTrue(rooms.findAll().size() >= 2);
    }
}
