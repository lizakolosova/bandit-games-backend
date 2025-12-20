package be.kdg.platform.adapter.in;

import be.kdg.platform.adapter.in.request.CreateGameRequest;
import be.kdg.platform.adapter.in.response.GameDetailsDto;
import be.kdg.platform.adapter.in.response.GameDto;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.*;

import be.kdg.platform.port.in.command.AchievementDefinitionCommand;
import be.kdg.platform.port.in.command.AddGameWithAchievementsCommand;
import be.kdg.platform.port.in.command.ApproveGameCommand;
import be.kdg.platform.port.in.command.ViewGameCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final FindAllGamesPort findAllGamesPort;
    private final ViewGameUseCase viewGameUseCase;
    private final AddGameWithAchievementsUseCase addGameWithAchievementsUseCase;
    private final ApproveGameUseCase approveGameUseCase;

    public GameController(FindAllGamesPort findAllGamesPort, ViewGameUseCase viewGameUseCase, AddGameWithAchievementsUseCase addGameWithAchievementsUseCase, ApproveGameUseCase approveGameUseCase) {
        this.findAllGamesPort = findAllGamesPort;
        this.viewGameUseCase = viewGameUseCase;
        this.addGameWithAchievementsUseCase = addGameWithAchievementsUseCase;
        this.approveGameUseCase = approveGameUseCase;
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
    public ResponseEntity<GameDto> create(@RequestBody CreateGameRequest request) {

        AddGameWithAchievementsCommand command = new AddGameWithAchievementsCommand(request.name(), request.rules(), request.pictureUrl(),
                request.gameUrl(), request.category(), request.developedBy(), request.createdAt(), request.averageMinutes(),
                request.achievements().stream()
                        .map(a -> new AchievementDefinitionCommand(a.name(), a.description()))
                        .toList()
        );

        Game game = addGameWithAchievementsUseCase.create(command);

        return ResponseEntity.ok(GameDto.toDto(game));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable UUID id) throws GameNotFoundException {
        approveGameUseCase.approve(new ApproveGameCommand(id));
        return ResponseEntity.noContent().build();
    }
}
