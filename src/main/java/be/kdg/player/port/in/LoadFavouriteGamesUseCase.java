package be.kdg.player.port.in;

import be.kdg.player.domain.valueobj.FavouriteGame;
import java.util.List;

public interface LoadFavouriteGamesUseCase {
    List<FavouriteGame> loadFavourites(FavouriteGamesCommand command);
}
