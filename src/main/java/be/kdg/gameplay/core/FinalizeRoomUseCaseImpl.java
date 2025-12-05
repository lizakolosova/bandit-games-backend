package be.kdg.gameplay.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.FinalizeRoomCommand;
import be.kdg.gameplay.port.in.FinalizeRoomUseCase;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FinalizeRoomUseCaseImpl implements FinalizeRoomUseCase {

    private final LoadGameRoomPort loadGameRoomPort;
    private final UpdateGameRoomPort updateGameRoomPort;
    private final GameplayEventPublisher eventPublisher;

    public FinalizeRoomUseCaseImpl(LoadGameRoomPort loadGameRoomPort,
                                   UpdateGameRoomPort updateGameRoomPort,
                                   GameplayEventPublisher eventPublisher) {
        this.loadGameRoomPort = loadGameRoomPort;
        this.updateGameRoomPort = updateGameRoomPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public GameRoom finalize(FinalizeRoomCommand command) {
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(command.gameRoomId()));

        GameRoom saved = updateGameRoomPort.update(room.finalizeRoom(PlayerId.of(command.playerId())));

        eventPublisher.publishEvents(room.pullDomainEvents());
        return saved;
    }
}
