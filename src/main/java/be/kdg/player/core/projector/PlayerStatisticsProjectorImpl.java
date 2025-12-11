package be.kdg.player.core.projector;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.PlayerStatisticsProjector;
import be.kdg.player.port.in.command.UpdatePlayerStatisticsCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerStatisticsProjectorImpl implements PlayerStatisticsProjector {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;

    public PlayerStatisticsProjectorImpl(LoadPlayerPort loadPlayerPort,
                                         UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public void project(UpdatePlayerStatisticsCommand command) {
        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId())).orElseThrow(()->
                NotFoundException.player(command.playerId()));

        player.recordGameResult(command.gameId(), command.playerId(), command.startedAt(), command.finishedAt(), command.winnerId());

        updatePlayerPort.update(player);
    }
}