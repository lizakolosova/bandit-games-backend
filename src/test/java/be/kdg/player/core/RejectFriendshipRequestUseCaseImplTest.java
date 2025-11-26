package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.domain.valueobj.FriendshipStatus;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.in.RejectFriendshipRequestCommand;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.UpdateFriendshipRequestPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.never;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RejectFriendshipRequestUseCaseImplTest {

    @Mock
    private LoadFriendshipRequestPort loadPort;

    @Mock
    private UpdateFriendshipRequestPort updatePort;

    private RejectFriendshipRequestUseCaseImpl sut;

    @BeforeEach
    void setup() {
        sut = new RejectFriendshipRequestUseCaseImpl(loadPort, updatePort);
    }
    @Test
    void ShouldRejectFriendshipRequest() {
        // Arrange
        FriendshipRequestId requestId = FriendshipRequestId.create();
        SenderId sender = SenderId.of(PlayerId.create());
        ReceiverId receiver = ReceiverId.of(PlayerId.create());

        var friendshipRequest = new FriendshipRequest(
                requestId,
                sender,
                receiver,
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        when(loadPort.load(requestId)).thenReturn(friendshipRequest);
        when(updatePort.update(friendshipRequest)).thenReturn(friendshipRequest);

        var command = new RejectFriendshipRequestCommand(
                requestId.uuid(),
                receiver.uuid()
        );

        // Act
        FriendshipRequest result = sut.reject(command);

        // Assert
        assertEquals(FriendshipStatus.REJECTED, result.getStatus());
        verify(loadPort).load(requestId);
        verify(updatePort).update(friendshipRequest);
    }

    @Test
    void ShouldNotRejectIfWrongReceiver() {
        // Arrange
        FriendshipRequestId requestId = FriendshipRequestId.create();
        SenderId sender = SenderId.of(PlayerId.create());
        ReceiverId receiver = ReceiverId.of(PlayerId.create());
        ReceiverId wrongReceiver = ReceiverId.of(PlayerId.create());

        FriendshipRequest friendshipRequest = new FriendshipRequest(
                requestId,
                sender,
                receiver,
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        when(loadPort.load(requestId)).thenReturn(friendshipRequest);

        RejectFriendshipRequestCommand command = new RejectFriendshipRequestCommand(
                requestId.uuid(),
                wrongReceiver.uuid()
        );

        // Act
        Executable action = () -> sut.reject(command);

        // Assert
        assertThrows(IllegalStateException.class, action);
        verify(updatePort, never()).update(any());
    }

    @Test
    void ShouldNotRejectIfNotPending() {
        // Arrange
        FriendshipRequestId requestId = FriendshipRequestId.create();
        SenderId sender = SenderId.of(PlayerId.create());
        ReceiverId receiver = ReceiverId.of(PlayerId.create());

        var friendshipRequest = new FriendshipRequest(
                requestId,
                sender,
                receiver,
                FriendshipStatus.ACCEPTED,
                LocalDateTime.now()
        );

        when(loadPort.load(requestId)).thenReturn(friendshipRequest);

        var command = new RejectFriendshipRequestCommand(
                requestId.uuid(),
                receiver.uuid()
        );

        // Act
        Executable action = () -> sut.reject(command);

        // Assert
        assertThrows(IllegalStateException.class, action);
        verify(updatePort, never()).update(any());
    }

    @Test
    void ShouldThrowIfFriendshipRequestNotFound() {
        // Arrange
        FriendshipRequestId requestId = FriendshipRequestId.create();
        ReceiverId receiverId = ReceiverId.of(PlayerId.create());

        when(loadPort.load(requestId))
                .thenThrow(new IllegalArgumentException("Friendship request not found"));

        RejectFriendshipRequestCommand command = new RejectFriendshipRequestCommand(
                requestId.uuid(),
                receiverId.uuid()
        );

        // Act
        Executable action = () -> sut.reject(command);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
        verify(updatePort, never()).update(any());
    }
}
