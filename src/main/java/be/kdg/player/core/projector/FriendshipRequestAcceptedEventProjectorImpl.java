package be.kdg.player.core.projector;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.FriendshipRequestAcceptedProjectionCommand;
import be.kdg.player.port.in.FriendshipRequestAcceptedProjector;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FriendshipRequestAcceptedEventProjectorImpl implements FriendshipRequestAcceptedProjector {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;

    public FriendshipRequestAcceptedEventProjectorImpl(LoadPlayerPort loadPlayerPort, UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public void project(FriendshipRequestAcceptedProjectionCommand command) {
        Player sender = loadPlayerPort.loadById(PlayerId.of(command.senderId())).orElseThrow(()-> NotFoundException.player(command.senderId()));
        Player receiver = loadPlayerPort.loadById(PlayerId.of(command.receiverId())).orElseThrow(()-> NotFoundException.player(command.receiverId()));

        sender.addFriend(receiver.getPlayerId().uuid());
        receiver.addFriend(sender.getPlayerId().uuid());

        updatePlayerPort.update(sender);
        updatePlayerPort.update(receiver);
    }
}
