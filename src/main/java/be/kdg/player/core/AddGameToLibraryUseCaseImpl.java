package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.port.in.AddGameToLibraryCommand;
import be.kdg.player.port.in.AddGameToLibraryUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AddGameToLibraryUseCaseImpl implements AddGameToLibraryUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final UpdatePlayerPort updatePlayerPort;

    public AddGameToLibraryUseCaseImpl(LoadPlayerPort loadPlayerPort,
                                       UpdatePlayerPort updatePlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public GameLibrary addGameToLibrary(AddGameToLibraryCommand command) {

        PlayerId playerId = PlayerId.of(command.playerId());

        Player player = loadPlayerPort.loadById(playerId)
                .orElseThrow(() -> new IllegalStateException("Player not found: " + command.playerId()));

        GameLibrary library = player.addGameToLibrary(command.gameId());
        updatePlayerPort.update(player);
        return library;
    }
}