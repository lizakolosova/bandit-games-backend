package be.kdg.platform.core;

import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.FilterGamesCommand;
import be.kdg.platform.port.in.FilterGamesUseCase;
import be.kdg.platform.port.out.LoadGamePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterGamesUseCaseImpl implements FilterGamesUseCase {

    private final LoadGamePort loadGamePort;

    public FilterGamesUseCaseImpl(LoadGamePort loadGamePort) {
        this.loadGamePort = loadGamePort;
    }

    @Override
    public List<Game> filter(FilterGamesCommand command) {
        List<Game> allGames = loadGamePort.loadAll();

        return allGames.stream()
                .filter(g -> command.category().isEmpty() || g.getCategory().equalsIgnoreCase(command.category().get()))
                .toList();
    }
}
