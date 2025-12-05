package be.kdg.gameplay.core;

import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.StartMatchCommand;
import be.kdg.gameplay.port.in.StartMatchUseCase;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StartMatchUseCaseImpl implements StartMatchUseCase {

    private final LoadGameRoomPort loadGameRoomPort;
    private final UpdateGameRoomPort updateGameRoomPort;
    private final AddMatchPort addMatchPort;
    private final GameplayEventPublisher eventPublisher;

    public StartMatchUseCaseImpl(LoadGameRoomPort loadGameRoomPort,
                                 UpdateGameRoomPort updateGameRoomPort,
                                 AddMatchPort addMatchPort, GameplayEventPublisher eventPublisher) {
        this.loadGameRoomPort = loadGameRoomPort;
        this.updateGameRoomPort = updateGameRoomPort;
        this.addMatchPort = addMatchPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public GameRoom start(StartMatchCommand command) {
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(command.gameRoomId()));

        GameRoom gameRoom = room.finalizeRoom();

        GameRoom saved = updateGameRoomPort.update(room);

        eventPublisher.publishEvents(room.pullDomainEvents());
        return saved;
    }
}
