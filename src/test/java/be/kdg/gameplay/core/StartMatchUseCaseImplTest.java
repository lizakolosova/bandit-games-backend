//package be.kdg.gameplay.core;
//
//import be.kdg.common.exception.GameRoomException;
//import be.kdg.common.valueobj.GameId;
//import be.kdg.common.valueobj.PlayerId;
//import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
//import be.kdg.gameplay.domain.GameRoom;
//import be.kdg.gameplay.domain.Match;
//import be.kdg.gameplay.domain.valueobj.*;
//import be.kdg.gameplay.port.in.FinalizeRoomCommand;
//import be.kdg.gameplay.port.out.AddMatchPort;
//import be.kdg.gameplay.port.out.LoadGameRoomPort;
//import be.kdg.gameplay.port.out.UpdateGameRoomPort;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class StartMatchUseCaseImplTest {
//
//    @Mock
//    private LoadGameRoomPort loadGameRoomPort;
//
//    @Mock
//    private UpdateGameRoomPort updateGameRoomPort;
//
//    @Mock
//    private AddMatchPort addMatchPort;
//
//    @Mock
//    private GameplayEventPublisher eventPublisher;
//
//    @InjectMocks
//    private FinalizeRoomUseCaseImpl useCase;
//
//    private GameRoom gameRoom;
//
//    @BeforeEach
//    void setUp() {
//        UUID gameId = UUID.randomUUID();
//        UUID hostId = UUID.randomUUID();
//        UUID invitedId = UUID.randomUUID();
//
//        gameRoom = new GameRoom(
//                GameId.of(gameId),
//                PlayerId.of(hostId),
//                PlayerId.of(invitedId),
//                GameRoomType.CLOSED
//        );
//    }
//
//    @Test
//    void shouldLoadRoomStartMatchUpdateRoomAndPersistMatch() {
//        // Arrange
//        gameRoom.setStatus(GameRoomStatus.READY);
//        UUID roomId = gameRoom.getGameRoomId().uuid();
//        FinalizeRoomCommand command = new FinalizeRoomCommand(roomId);
//
//        when(loadGameRoomPort.loadById(GameRoomId.of(roomId)))
//                .thenReturn(gameRoom);
//
//        ArgumentCaptor<GameRoom> updatedRoomCaptor = ArgumentCaptor.forClass(GameRoom.class);
//        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
//
//        when(addMatchPort.add(any(Match.class)))
//                .thenAnswer(inv -> inv.getArgument(0));
//        doNothing().when(eventPublisher).publishEvents(anyList());
//        // Act
//        Match result = useCase.finalize(command);
//
//        // Assert
//        verify(loadGameRoomPort).loadById(GameRoomId.of(roomId));
//
//        verify(updateGameRoomPort).update(updatedRoomCaptor.capture());
//        GameRoom updatedRoom = updatedRoomCaptor.getValue();
//        assertEquals("MATCH_STARTED", updatedRoom.getStatus().name());
//
//        verify(addMatchPort).add(matchCaptor.capture());
//        Match persisted = matchCaptor.getValue();
//
//        assertNotNull(persisted);
//        assertSame(persisted, result);
//
//        assertEquals(MatchStatus.IN_PROGRESS, result.getStatus());
//        assertEquals(gameRoom.getGameId(), result.getGameId());
//
//        assertEquals(2, result.getPlayers().size());
//        assertTrue(result.getPlayers().contains(gameRoom.getHostPlayerId()));
//        assertTrue(result.getPlayers().contains(gameRoom.getInvitedPlayerId()));
//    }
//    @Test
//    void shouldThrowExceptionWhenGameRoomIsNotReady() {
//        // Arrange
//        UUID roomId = gameRoom.getGameRoomId().uuid();
//        FinalizeRoomCommand command = new FinalizeRoomCommand(roomId);
//
//        // NOT READY (default status of new GameRoom)
//        assertNotEquals(GameRoomStatus.READY, gameRoom.getStatus());
//
//        when(loadGameRoomPort.loadById(GameRoomId.of(roomId)))
//                .thenReturn(gameRoom);
//
//        // Act + Assert
//        assertThrows(
//                GameRoomException.class,
//                () -> useCase.finalize(command)
//        );
//
//        // Verify no side effects
//        verify(updateGameRoomPort, never()).update(any());
//        verify(addMatchPort, never()).add(any());
//        verify(eventPublisher, never()).publishEvents(anyList());
//
//        verify(loadGameRoomPort).loadById(GameRoomId.of(roomId));
//    }
//}