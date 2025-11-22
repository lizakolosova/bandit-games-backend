package be.kdg.player.adapter.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.request.AddGameToLibraryRequest;
import be.kdg.player.adapter.in.response.FriendDto;
import be.kdg.player.adapter.in.response.GameLibraryDto;
import be.kdg.player.adapter.in.response.LibraryGameDetailsDto;
import be.kdg.player.port.in.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final LoadGameLibraryUseCase loadGameLibraryUseCase;
    private final AddGameToLibraryUseCase addGameToLibraryUseCase;
    private final LoadLibraryGameUseCase loadLibraryGameUseCase;
    private final LoadFriendsUseCase loadFriendsUseCase;

    public PlayerController(LoadGameLibraryUseCase loadGameLibraryUseCase, AddGameToLibraryUseCase addGameToLibraryUseCase, LoadLibraryGameUseCase loadLibraryGameUseCase, LoadFriendsUseCase loadFriendsUseCase) {
        this.loadGameLibraryUseCase = loadGameLibraryUseCase;
        this.addGameToLibraryUseCase = addGameToLibraryUseCase;
        this.loadLibraryGameUseCase = loadLibraryGameUseCase;
        this.loadFriendsUseCase = loadFriendsUseCase;
    }

    @GetMapping("/library")
    public ResponseEntity<List<GameLibraryDto>> loadLibrary(@AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        LoadGameLibraryCommand query = new LoadGameLibraryCommand(playerId);

        List<GameLibraryDto> result = loadGameLibraryUseCase.loadLibrary(query)
                .stream()
                .map(gl -> new GameLibraryDto(
                        gl.getGameId(),
                        gl.getAddedAt(),
                        gl.getLastPlayedAt(),
                        gl.getTotalPlaytime() == null ? 0 : gl.getTotalPlaytime().toMinutes(),
                        gl.isFavourite()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/library")
    public ResponseEntity<Void> addGameToLibrary(
            @RequestBody AddGameToLibraryRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());

        AddGameToLibraryCommand command = new AddGameToLibraryCommand(playerId, request.gameId());
        addGameToLibraryUseCase.addGameToLibrary(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/library/{gameId}")
    public ResponseEntity<LibraryGameDetailsDto> loadLibraryGame(
            @PathVariable UUID gameId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        LoadLibraryGameCommand command = new LoadLibraryGameCommand(playerId, gameId);
        return ResponseEntity.ok(loadLibraryGameUseCase.loadGame(command));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDto>> loadFriends(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(loadFriendsUseCase.loadFriends(PlayerId.of(playerId))); // ask teacher about this
    }
}
