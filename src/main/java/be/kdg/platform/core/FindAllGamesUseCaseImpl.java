package be.kdg.platform.core;

import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.FindAllGamesPort;
import be.kdg.platform.port.out.LoadGamePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllGamesUseCaseImpl implements FindAllGamesPort {

    private final LoadGamePort loadGamePort;

    public FindAllGamesUseCaseImpl(LoadGamePort loadGamePort) {
        this.loadGamePort = loadGamePort;
    }

    @Override
    public List<Game> findAll() {
        return this.loadGamePort.loadAll();
    }
}
