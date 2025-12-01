package be.kdg.gameplay.core;

import be.kdg.TestHelper;
import be.kdg.common.exception.GameRoomException;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.config.TestContainersConfig;
import be.kdg.gameplay.adapter.out.GameRoomJpaEntity;
import be.kdg.gameplay.adapter.out.GameRoomJpaRepository;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomStatus;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.AcceptInvitationCommand;
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
class AcceptInvitationUseCaseImplIntegrationTest {

    @Autowired
    private AcceptInvitationUseCaseImpl sut;

    @Autowired
    private AddGameRoomPort addGameRoomPort;

    @Autowired
    private GameRoomJpaRepository rooms;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldAllowInvitedPlayerToAcceptInvitation() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                GameId.of(gameId),
                PlayerId.of(hostId),
                PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);

        UUID roomId = room.getGameRoomId().uuid();

        var command = new AcceptInvitationCommand(
                roomId,
                invitedId
        );

        // Act
        sut.accept(command);

        // Assert
        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();

        // Domain expectation — adjust if your domain uses another accepted status
        assertEquals(GameRoomStatus.READY, saved.getStatus());
        assertEquals(saved.getInvitedPlayerId(), invitedId);
    }

    @Test
    void shouldNotChangeRoomWhenUnknownPlayerAccepts() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                GameId.of(gameId),
                PlayerId.of(hostId),
                PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);
        UUID roomId = room.getGameRoomId().uuid();

        UUID unknownPlayer = UUID.randomUUID();

        var command = new AcceptInvitationCommand(
                roomId,
                unknownPlayer
        );

        // Act / Assert
        assertThrows(GameRoomException.class, () -> sut.accept(command));

        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();
        assertEquals(GameRoomStatus.WAITING, saved.getStatus());
        assertEquals(invitedId, saved.getInvitedPlayerId());
    }

    @Test
    void shouldPersistChangesInDatabase() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();

        GameRoom room = new GameRoom(
                GameId.of(gameId),
                PlayerId.of(hostId),
                PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );

        addGameRoomPort.add(room);
        UUID roomId = room.getGameRoomId().uuid();
        var command = new AcceptInvitationCommand(
                roomId,
                invitedId
        );

        // Act
        sut.accept(command);

        // Assert DB persistence
        GameRoomJpaEntity saved = rooms.findById(roomId).orElseThrow();
        assertEquals(GameRoomStatus.READY, saved.getStatus());
    }
}