package be.kdg.platform.port.in;
import be.kdg.platform.domain.Game;

import java.util.List;

public interface FindAllGamesPort {

    List<Game> findAll();
}
