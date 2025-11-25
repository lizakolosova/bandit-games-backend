package be.kdg.player.core;

import be.kdg.TestHelper;
import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.in.SendFriendshipRequestCommand;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.AddFriendshipRequestPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SendFriendshipRequestUseCaseImplTest {

    private SendFriendshipRequestUseCaseImpl sut;

    @Mock
    private LoadPlayerPort loadPlayerPort;

    @Mock
    private AddFriendshipRequestPort addFriendshipRequestPort;

    @Mock
    private LoadFriendshipRequestPort loadFriendshipRequestPort;


    private TestHelper testHelper;

    @BeforeEach
    void setUp() {
        sut = new SendFriendshipRequestUseCaseImpl(loadPlayerPort, addFriendshipRequestPort, loadFriendshipRequestPort);
        testHelper = new TestHelper();
    }


    @Test
    void shouldSendRequestSuccessfully() {
        // Arrange
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        SendFriendshipRequestCommand command =
                new SendFriendshipRequestCommand(senderId, receiverId);

        when(loadPlayerPort.loadById(PlayerId.of(senderId)))
                .thenReturn(Optional.of(testHelper.createDummyPlayer(senderId)));

        when(loadPlayerPort.loadById(PlayerId.of(receiverId)))
                .thenReturn(Optional.of(testHelper.createDummyPlayer(receiverId)));

        when(loadFriendshipRequestPort.existsPending(SenderId.of(senderId), ReceiverId.of(receiverId)))
                .thenReturn(false);

        when(loadFriendshipRequestPort.existsPending(
                SenderId.of(senderId), ReceiverId.of(receiverId)))
                .thenReturn(false);
        // Act
        FriendshipRequest result = sut.sendRequest(command);

        // Assert
        assertNotNull(result);
        assertEquals(senderId, result.getSenderId().uuid());
        assertEquals(receiverId, result.getReceiverId().uuid());

        verify(addFriendshipRequestPort).save(any());
    }


    @Test
    void shouldThrowWhenSenderNotFound() {
        // Arrange
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        SendFriendshipRequestCommand command =
                new SendFriendshipRequestCommand(senderId, receiverId);

        when(loadPlayerPort.loadById(PlayerId.of(senderId)))
                .thenReturn(Optional.empty());

        // Act
        Executable action = () -> sut.sendRequest(command);

        // Assert
        assertThrows(NotFoundException.class, action);
        verify(addFriendshipRequestPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenReceiverNotFound() {
        // Arrange
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        SendFriendshipRequestCommand command =
                new SendFriendshipRequestCommand(senderId, receiverId);

        when(loadPlayerPort.loadById(PlayerId.of(senderId)))
                .thenReturn(Optional.of(testHelper.createDummyPlayer(senderId)));

        when(loadPlayerPort.loadById(PlayerId.of(receiverId)))
                .thenReturn(Optional.empty());

        // Act
        Executable action = () -> sut.sendRequest(command);

        // Assert
        assertThrows(NotFoundException.class, action);
        verify(addFriendshipRequestPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenRequestAlreadyExists() {
        // Arrange
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        SendFriendshipRequestCommand command =
                new SendFriendshipRequestCommand(senderId, receiverId);

        when(loadPlayerPort.loadById(PlayerId.of(senderId)))
                .thenReturn(Optional.of(testHelper.createDummyPlayer(senderId)));

        when(loadPlayerPort.loadById(PlayerId.of(receiverId)))
                .thenReturn(Optional.of(testHelper.createDummyPlayer(receiverId)));

        when(loadFriendshipRequestPort.existsPending(SenderId.of(senderId), ReceiverId.of(receiverId)))
                .thenReturn(true);

        // Act
        Executable action = () -> sut.sendRequest(command);

        // Assert
        assertThrows(IllegalStateException.class, action);
        verify(addFriendshipRequestPort, never()).save(any());
    }
}
