package be.kdg.player.port.in;

import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.SearchPlayersCommand;

import java.util.List;

public interface SearchPlayersUseCase {
    List<Player> search(SearchPlayersCommand command);
}

