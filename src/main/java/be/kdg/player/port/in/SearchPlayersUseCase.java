package be.kdg.player.port.in;

import be.kdg.player.domain.Player;
import java.util.List;

public interface SearchPlayersUseCase {
    List<Player> search(SearchPlayersCommand command);
}

