package be.kdg.player.adapter.in;

import be.kdg.player.adapter.in.response.FavouriteGameDto;
import be.kdg.player.port.in.FavouriteGamesCommand;
import be.kdg.player.port.in.LoadFavouriteGamesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final LoadFavouriteGamesUseCase loadFavouriteGamesUseCase;

    public PlayerController(LoadFavouriteGamesUseCase loadFavouriteGamesUseCase) {
        this.loadFavouriteGamesUseCase = loadFavouriteGamesUseCase;
    }

    @GetMapping("/{playerId}/favourites")
    public ResponseEntity<List<FavouriteGameDto>> loadFavourites(@PathVariable UUID playerId) {

        var query = new FavouriteGamesCommand(playerId);

        List<FavouriteGameDto> result = loadFavouriteGamesUseCase.loadFavourites(query)
                .stream().map(game -> new FavouriteGameDto(
                        game.gameId(),
                        game.lastPlayedAt(),
                        game.totalPlaytime() == null ? 0 : game.totalPlaytime().toMinutes())).toList();

        return ResponseEntity.ok(result);
    }
}
