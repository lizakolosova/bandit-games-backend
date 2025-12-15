package be.kdg.gameplay.core;

import be.kdg.common.exception.GameRoomException;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.*;
import be.kdg.gameplay.port.in.FinalizeRoomCommand;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartMatchUseCaseImplTest {

    @Mock
    private LoadGameRoomPort loadGameRoomPort;

    @Mock
    private UpdateGameRoomPort updateGameRoomPort;

    @Mock
    private AddMatchPort addMatchPort;

    @Mock
    private GameplayEventPublisher eventPublisher;

    @InjectMocks
    private FinalizeRoomUseCaseImpl useCase;

    private GameRoom gameRoom;

    @BeforeEach
    void setUp() {
        UUID gameId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();
        String invitedName = UUID.randomUUID().toString();
        String hostName = UUID.randomUUID().toString();

        gameRoom = new GameRoom(
                GameId.of(gameId),
                hostName,
                invitedName,
                PlayerId.of(hostId),
                PlayerId.of(invitedId),
                GameRoomType.CLOSED
        );
    }

    @Test
    void shouldFinalizeTheRoom() {
        // Arrange
        gameRoom.setStatus(GameRoomStatus.READY);
        UUID roomId = gameRoom.getGameRoomId().uuid();
        FinalizeRoomCommand command = new FinalizeRoomCommand(roomId, gameRoom.getHostPlayerId().uuid());

        when(loadGameRoomPort.loadById(GameRoomId.of(roomId)))
                .thenReturn(gameRoom);

        when(updateGameRoomPort.update(any(GameRoom.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(eventPublisher).publishEvents(anyList());

        // Act
        GameRoom result = useCase.finalize(command);

        // Assert
        verify(loadGameRoomPort).loadById(GameRoomId.of(roomId));
        verify(updateGameRoomPort).update(any(GameRoom.class));

        assertNotNull(result);
        assertEquals("FINALIZED", result.getStatus().name());
        assertEquals(gameRoom.getGameId(), result.getGameId());
        assertEquals(gameRoom.getHostPlayerId(), result.getHostPlayerId());
        assertEquals(gameRoom.getInvitedPlayerId(), result.getInvitedPlayerId());
    }

    @Test
    void shouldThrowExceptionWhenGameRoomIsNotReady() {
        // Arrange
        UUID roomId = gameRoom.getGameRoomId().uuid();
        FinalizeRoomCommand command = new FinalizeRoomCommand(roomId, gameRoom.getHostPlayerId().uuid());

        assertNotEquals(GameRoomStatus.READY, gameRoom.getStatus());

        when(loadGameRoomPort.loadById(GameRoomId.of(roomId)))
                .thenReturn(gameRoom);

        // Act + Assert
        assertThrows(
                GameRoomException.class,
                () -> useCase.finalize(command)
        );

        // Verify
        verify(updateGameRoomPort, never()).update(any());
        verify(addMatchPort, never()).add(any());
        verify(eventPublisher, never()).publishEvents(anyList());

        verify(loadGameRoomPort).loadById(GameRoomId.of(roomId));
    }
}