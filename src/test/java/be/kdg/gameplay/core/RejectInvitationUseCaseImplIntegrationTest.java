package be.kdg.gameplay.core;

import be.kdg.TestHelper;
import be.kdg.common.exception.GameRoomException;
import be.kdg.config.TestContainersConfig;
import be.kdg.gameplay.adapter.out.GameRoomJpaEntity;
import be.kdg.gameplay.adapter.out.GameRoomJpaRepository;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.RejectInvitationCommand;
import be.kdg.gameplay.port.out.AddGameRoomPort;
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
class RejectInvitationUseCaseImplIntegrationTest {

    @Autowired
    private RejectInvitationUseCaseImpl sut;

    @Autowired
    private AddGameRoomPort addGameRoomPort;

    @Autowired
    private GameRoomJpaRepository rooms;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void cleanup() {
        testHelper.cleanUp();
    }

    @Test
    void shouldAllowInvitedPlayerToRejectInvitation() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                be.kdg.common.valueobj.GameId.of(gameId),
                be.kdg.common.valueobj.PlayerId.of(hostId),
                be.kdg.common.valueobj.PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);
        UUID roomId = room.getGameRoomId().uuid();

        var command = new RejectInvitationCommand(roomId, invitedId);

        // Act
        sut.reject(command);

        // Assert
        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();
        assertEquals(GameRoomStatus.WAITING, saved.getStatus());
    }

    @Test
    void shouldNotAllowUnknownPlayerToReject() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                be.kdg.common.valueobj.GameId.of(gameId),
                be.kdg.common.valueobj.PlayerId.of(hostId),
                be.kdg.common.valueobj.PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);
        UUID roomId = room.getGameRoomId().uuid();

        UUID unknown = UUID.randomUUID();

        var command = new RejectInvitationCommand(roomId, unknown);

        // Act + Assert
        assertThrows(
                GameRoomException.class,
                () -> sut.reject(command),
                "Only the invited player should be allowed to reject"
        );

        // DB state must remain unchanged
        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();
        assertEquals(GameRoomStatus.WAITING, saved.getStatus());
        assertEquals(invitedId, saved.getInvitedPlayerId());
    }

    @Test
    void shouldPersistRejectionToDatabase() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                be.kdg.common.valueobj.GameId.of(gameId),
                be.kdg.common.valueobj.PlayerId.of(hostId),
                be.kdg.common.valueobj.PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);
        UUID roomId = room.getGameRoomId().uuid();

        var command = new RejectInvitationCommand(roomId, invitedId);

        // Act
        sut.reject(command);

        // Assert persistence
        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();
        assertEquals(GameRoomStatus.WAITING, saved.getStatus());
    }
}