package be.kdg.platform.adapter.in;

import be.kdg.platform.adapter.in.response.AddGameRequest;
import be.kdg.platform.adapter.in.response.FilterGameDto;
import be.kdg.platform.adapter.in.response.GameDetailsDto;
import be.kdg.platform.adapter.in.response.GameDto;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.*;

import be.kdg.player.port.in.MarkFavouriteCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final FindAllGamesPort findAllGamesPort;
    private final ViewGameUseCase viewGameUseCase;
    private final AddGameUseCase addGameUseCase;
    private final FilterGamesUseCase filterGamesUseCase;

    public GameController(FindAllGamesPort findAllGamesPort, ViewGameUseCase viewGameUseCase, AddGameUseCase addGameUseCase, FilterGamesUseCase filterGamesUseCase) {
        this.findAllGamesPort = findAllGamesPort;
        this.viewGameUseCase = viewGameUseCase;
        this.addGameUseCase = addGameUseCase;
        this.filterGamesUseCase = filterGamesUseCase;
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> findAllGames() {
        List<GameDto> games = findAllGamesPort.findAll()
                .stream()
                .map(GameDto::toDto)
                .toList();

        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetailsDto> viewGame(@PathVariable String id)
            throws GameNotFoundException {
        ViewGameCommand command = new ViewGameCommand(UUID.fromString(id));
        Game game = viewGameUseCase.viewGame(command);
        return ResponseEntity.ok(GameDetailsDto.toDetailsDto(game));
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody AddGameRequest request) {

        AddGameCommand command = new AddGameCommand(
                request.name(),
                request.rules(),
                request.pictureUrl(),
                request.gameUrl(),
                request.category(),
                request.developedBy(),
                request.createdAt(),
                request.averageMinutes()
        );

        Game game = addGameUseCase.createGame(command);

        return ResponseEntity.ok(GameDto.toDto(game));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FilterGameDto>> filterGames(
            @RequestParam Optional<String> category
    ) {

        FilterGamesCommand command = new FilterGamesCommand(category);

        List<FilterGameDto> games = filterGamesUseCase.filter(command).stream()
                .map(game -> new FilterGameDto(
                        game.getGameId().uuid().toString(),
                        game.getName(),
                        game.getPictureUrl(),
                        game.getCategory(),
                        game.getAverageMinutes()
                ))
                .toList();

        return ResponseEntity.ok(games);
    }
}
