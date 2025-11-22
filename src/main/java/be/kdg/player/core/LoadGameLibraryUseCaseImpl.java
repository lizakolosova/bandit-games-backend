package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.LoadGameLibraryCommand;
import be.kdg.player.port.in.LoadGameLibraryUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadGameLibraryUseCaseImpl implements LoadGameLibraryUseCase {

    private final LoadPlayerPort loadPlayerPort;

    public LoadGameLibraryUseCaseImpl(LoadPlayerPort loadPlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    @Transactional
    public List<GameLibrary> loadLibrary(LoadGameLibraryCommand query) {
        PlayerId id = PlayerId.of(query.playerId());

        Player player = loadPlayerPort.loadById(id)
                .orElseThrow(() -> new IllegalStateException("Player not found: " + id.uuid()));

        return player.getGameLibraries().stream().toList();
    }
}