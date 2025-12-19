package be.kdg.gameplay.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.in.command.RejectInvitationCommand;
import be.kdg.gameplay.port.in.RejectInvitationUseCase;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import org.springframework.stereotype.Service;

@Service
public class RejectInvitationUseCaseImpl implements RejectInvitationUseCase {

    private final LoadGameRoomPort loadGameRoomPort;
    private final UpdateGameRoomPort updateGameRoomPort;

    public RejectInvitationUseCaseImpl(LoadGameRoomPort loadGameRoomPort, UpdateGameRoomPort updateGameRoomPort) {
        this.loadGameRoomPort = loadGameRoomPort;
        this.updateGameRoomPort = updateGameRoomPort;
    }

    @Override
    public void reject(RejectInvitationCommand command) {
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(command.gameRoomId()));
        room.rejectInvitation(PlayerId.of(command.playerId()));
        updateGameRoomPort.update(room);
    }
}

