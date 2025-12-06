package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.MarkFavouriteCommand;
import be.kdg.player.port.in.MarkFavouriteUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MarkFavouriteUseCaseImpl implements MarkFavouriteUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;

    public MarkFavouriteUseCaseImpl(LoadPlayerPort loadPlayerPort,
                                    UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public void markFavourite(MarkFavouriteCommand command) {

        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId()))
                .orElseThrow();

        if (command.favourite()) {
            player.markGameAsFavourite(command.gameId());
        } else {
            player.unmarkGameAsFavourite(command.gameId());
        }

        updatePlayerPort.update(player);
    }
}