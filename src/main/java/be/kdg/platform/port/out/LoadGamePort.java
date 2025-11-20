package be.kdg.platform.port.out;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.Game;

import java.util.List;
import java.util.Optional;

public interface LoadGamePort {

    Optional<Game> loadById(GameId id);
    List<Game> loadAll();
}
