package be.kdg.player.core;

import be.kdg.TestHelper;
import be.kdg.config.TestContainersConfig;
import be.kdg.player.adapter.out.FriendshipRequestJpaEntity;
import be.kdg.player.adapter.out.PlayerJpaRepository;
import be.kdg.player.adapter.out.FriendshipRequestJpaRepository;
import be.kdg.player.adapter.out.mapper.PlayerJpaMapper;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.Player;

import be.kdg.common.valueobj.PlayerId;

import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.domain.valueobj.FriendshipStatus;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.in.AcceptFriendshipRequestCommand;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
class AcceptFriendshipUseCaseImplIntegrationTest {

    @Autowired
    private AcceptFriendshipRequestUseCaseImpl sut;

    @Autowired
    private PlayerJpaRepository players;

    @Autowired
    private FriendshipRequestJpaRepository requests;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void clean() {
        testHelper.cleanUp();
    }

    @Test
    @Transactional
    void shouldAcceptFriendshipAndAddPlayersToEachOtherFriendList() {

        // Arrange
        var senderId = PlayerId.create();
        var receiverId = PlayerId.create();

        var sender = new Player(senderId, "alice", "alice@gmail.com", "pic1.png", LocalDateTime.now());
        var receiver = new Player(receiverId, "bob", "bob@gmail.com", "pic2.png", LocalDateTime.now());

        players.save(PlayerJpaMapper.toEntity(sender));
        players.save(PlayerJpaMapper.toEntity(receiver));

        var requestId = FriendshipRequestId.create();

        var request = new FriendshipRequest(
                requestId,
                SenderId.of(senderId),
                ReceiverId.of(receiverId),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        requests.save( new FriendshipRequestJpaEntity(request.getFriendshipRequestId().uuid(), request.getSenderId().uuid(),
                request.getReceiverId().uuid(), request.getStatus(), request.getCreatedAt()));

        //  Act
        var command = new AcceptFriendshipRequestCommand(requestId.uuid(), receiverId.uuid());
        FriendshipRequest updated = sut.accept(command);

        //  Assert
        assertEquals(FriendshipStatus.ACCEPTED, updated.getStatus());

        var savedRequest = requests.findById(requestId.uuid()).orElseThrow();
        assertEquals(FriendshipStatus.ACCEPTED, savedRequest.getStatus());

        var savedSender = players.findById(senderId.uuid()).orElseThrow();
        var savedReceiver = players.findById(receiverId.uuid()).orElseThrow();

        assertTrue(
                savedSender.getFriends().stream()
                        .anyMatch(f -> f.getFriendId().equals(receiverId.uuid()))
        );

        assertTrue(
                savedReceiver.getFriends().stream()
                        .anyMatch(f -> f.getFriendId().equals(senderId.uuid()))
        );
    }

    @Test
    void shouldThrowWhenReceiverMismatch() {

        // Arrange
        var senderId = PlayerId.create();
        var receiverId = PlayerId.create();
        var someoneElseId = PlayerId.create();

        var requestId = FriendshipRequestId.create();

        var request = new FriendshipRequest(
                requestId,
                SenderId.of(senderId),
                ReceiverId.of(receiverId),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        requests.save(new FriendshipRequestJpaEntity(request.getFriendshipRequestId().uuid(), request.getSenderId().uuid(),
                request.getReceiverId().uuid(), request.getStatus(), request.getCreatedAt()));

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> sut.accept(new AcceptFriendshipRequestCommand(requestId.uuid(), someoneElseId.uuid())));
    }
}
