package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.LoadSingleGameLibraryUseCase;
import be.kdg.player.port.in.command.LoadSingleGameLibraryCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LoadSingleGameLibraryUseCaseImpl implements LoadSingleGameLibraryUseCase {

    private final LoadPlayerPort loadPlayerPort;

    public LoadSingleGameLibraryUseCaseImpl(LoadPlayerPort loadPlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    @Transactional
    public GameLibrary loadGame(LoadSingleGameLibraryCommand command) {
        PlayerId playerId = PlayerId.of(command.playerId());

        Player player = loadPlayerPort.loadById(playerId)
                .orElseThrow(() -> NotFoundException.player(playerId.uuid()));

        return player.findGameInLibrary(command.gameId());
    }
}
