package be.kdg.player.core.projector;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.FriendRemovedProjectionCommand;
import be.kdg.player.port.in.FriendRemovedProjector;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FriendRemovedEventProjectorImpl implements FriendRemovedProjector {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;

    public FriendRemovedEventProjectorImpl(LoadPlayerPort loadPlayerPort,
                                           UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public void project(FriendRemovedProjectionCommand command) {

        Player friend = loadPlayerPort.loadById(PlayerId.of(command.friendId()))
                .orElseThrow(() -> NotFoundException.player(command.friendId()));

        friend.removeFriend(command.playerId());

        updatePlayerPort.update(friend);
    }
}

