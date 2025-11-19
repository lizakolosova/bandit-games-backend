package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.valueobj.FavouriteGame;
import be.kdg.player.port.in.FavouriteGamesCommand;
import be.kdg.player.port.in.LoadFavouriteGamesUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadFavouriteGamesUseCaseImpl implements LoadFavouriteGamesUseCase {

    private final LoadPlayerPort loadPlayerPort;

    public LoadFavouriteGamesUseCaseImpl(LoadPlayerPort loadPlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    public List<FavouriteGame> loadFavourites(FavouriteGamesCommand query) {
        PlayerId id = PlayerId.of(query.playerId());

        Player player = loadPlayerPort.loadById(id)
                .orElseThrow(() -> new IllegalStateException("Player not found: " + id.uuid()));

        return player.getFavouriteGames().stream().toList();
    }
}
