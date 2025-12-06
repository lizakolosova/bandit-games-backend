package be.kdg.gameplay.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.command.AcceptInvitationCommand;
import be.kdg.gameplay.port.in.AcceptInvitationUseCase;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import org.springframework.stereotype.Service;

@Service
public class AcceptInvitationUseCaseImpl implements AcceptInvitationUseCase {

    private final LoadGameRoomPort loadGameRoomPort;
    private final UpdateGameRoomPort updateGameRoomPort;

    public AcceptInvitationUseCaseImpl(LoadGameRoomPort loadGameRoomPort, UpdateGameRoomPort updateGameRoomPort) {
        this.loadGameRoomPort = loadGameRoomPort;
        this.updateGameRoomPort = updateGameRoomPort;
    }

    @Override
    public void accept(AcceptInvitationCommand command) {
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(command.gameRoomId()));
        room.acceptInvitation(PlayerId.of(command.playerId()));
        updateGameRoomPort.update(room);
    }
}