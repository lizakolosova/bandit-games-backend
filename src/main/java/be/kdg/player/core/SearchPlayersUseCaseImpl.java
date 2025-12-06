package be.kdg.player.core;


import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.SearchPlayersCommand;
import be.kdg.player.port.in.SearchPlayersUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchPlayersUseCaseImpl implements SearchPlayersUseCase {

    private final LoadPlayerPort loadPlayerPort;

    public SearchPlayersUseCaseImpl(LoadPlayerPort loadPlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    @Transactional
    public List<Player> search(SearchPlayersCommand command) {
        return loadPlayerPort.searchExcluding(
                PlayerId.of(command.loggedInPlayerId()), command.query());
    }
}

