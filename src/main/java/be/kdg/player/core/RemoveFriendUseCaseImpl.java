package be.kdg.player.core;

import be.kdg.player.adapter.out.PlayerEventPublisher;
import be.kdg.player.domain.Player;
import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;

import be.kdg.player.port.in.RemoveFriendCommand;
import be.kdg.player.port.in.RemoveFriendUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveFriendUseCaseImpl implements RemoveFriendUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;
    private final PlayerEventPublisher eventPublisher;

    public RemoveFriendUseCaseImpl(LoadPlayerPort loadPlayerPort, UpdatePlayerPort updatePlayerPort, PlayerEventPublisher eventPublisher) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public void removeFriend(RemoveFriendCommand command) {
        Player player = loadPlayerPort
                .loadById(new PlayerId(command.playerId()))
                .orElseThrow(() -> NotFoundException.player(command.playerId()));

        player.removeFriend(command.friendId());

        updatePlayerPort.update(player);
        eventPublisher.publishEvents(player.pullDomainEvents());
    }
}
