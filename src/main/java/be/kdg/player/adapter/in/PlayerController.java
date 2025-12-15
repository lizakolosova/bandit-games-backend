package be.kdg.player.adapter.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.request.SendFriendRequestDto;
import be.kdg.player.adapter.in.response.*;
import be.kdg.player.adapter.in.request.RegisterPlayerRequest;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.*;
import be.kdg.player.port.in.command.*;
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
    private final LoadLibraryGameUseCase loadLibraryGameUseCase;
    private final LoadFriendsUseCase loadFriendsUseCase;
    private final RegisterPlayerUseCase registerPlayerUseCase;
    private final MarkFavouriteUseCase markFavouriteUseCase;
    private final SendFriendshipRequestUseCase sendFriendshipRequestUseCase;
    private final SearchPlayersUseCase searchPlayersUseCase;
    private final RemoveFriendUseCase removeFriendUseCase;
    private final LoadSingleGameLibraryUseCase loadSingleGameLibraryUseCase;

    public PlayerController(LoadGameLibraryUseCase loadGameLibraryUseCase, LoadLibraryGameUseCase loadLibraryGameUseCase, LoadFriendsUseCase loadFriendsUseCase, RegisterPlayerUseCase registerPlayerUseCase, MarkFavouriteUseCase markFavouriteUseCase, SendFriendshipRequestUseCase sendFriendshipRequestUseCase, SearchPlayersUseCase searchPlayersUseCase, RemoveFriendUseCase removeFriendUseCase, LoadSingleGameLibraryUseCase loadSingleGameLibraryUseCase) {
        this.loadGameLibraryUseCase = loadGameLibraryUseCase;
        this.loadLibraryGameUseCase = loadLibraryGameUseCase;
        this.loadFriendsUseCase = loadFriendsUseCase;
        this.registerPlayerUseCase = registerPlayerUseCase;
        this.markFavouriteUseCase = markFavouriteUseCase;
        this.sendFriendshipRequestUseCase = sendFriendshipRequestUseCase;
        this.searchPlayersUseCase = searchPlayersUseCase;
        this.removeFriendUseCase = removeFriendUseCase;
        this.loadSingleGameLibraryUseCase = loadSingleGameLibraryUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerDto> registerPlayer(@RequestBody RegisterPlayerRequest request) {

        RegisterPlayerCommand command =
                new RegisterPlayerCommand(request.username(), request.email());

        Player result = registerPlayerUseCase.register(command);

        return ResponseEntity.status(201).body(new PlayerDto(result.getPlayerId().uuid(), result.getUsername(),
                result.getEmail(), result.getPictureUrl(), result.getCreatedAt()));
    }


    @GetMapping("/library")
    public ResponseEntity<List<GameLibraryDto>> loadLibrary(@AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        LoadGameLibraryCommand query = new LoadGameLibraryCommand(playerId);

        List<GameLibraryDto> result = loadGameLibraryUseCase.loadLibrary(query)
                .stream()
                .map(gl -> new GameLibraryDto(gl.getGameId(), gl.getPurchasedAt(), gl.getLastPlayedAt(),
                        gl.getTotalPlaytime() == null ? 0 : gl.getTotalPlaytime().toMinutes(), gl.isFavourite()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/library/{gameId}")
    public ResponseEntity<LibraryGameDetailsDto> loadLibraryGame(@PathVariable UUID gameId, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        LoadLibraryGameCommand command = new LoadLibraryGameCommand(playerId, gameId);
        return ResponseEntity.ok(loadLibraryGameUseCase.loadGame(command));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDto>> loadFriends(@AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        List<Player> friends = loadFriendsUseCase.loadFriends(PlayerId.of(playerId));
        return ResponseEntity.ok(friends.stream().map(friend -> new FriendDto(friend.getPlayerId().uuid(),
                friend.getUsername(), friend.getPictureUrl())).toList());
    }

    @PostMapping("/library/{gameId}/favourite")
    public ResponseEntity<Void> toggleFavourite(@PathVariable UUID gameId, @AuthenticationPrincipal Jwt jwt,
                                                @RequestParam boolean favourite) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        MarkFavouriteCommand command = new MarkFavouriteCommand(playerId, gameId, favourite);
        markFavouriteUseCase.markFavourite(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/requests")
    public ResponseEntity<Void> sendFriendRequest(@AuthenticationPrincipal Jwt jwt, @RequestBody SendFriendRequestDto request) {
        UUID senderId = UUID.fromString(jwt.getSubject());
        SendFriendshipRequestCommand command = new SendFriendshipRequestCommand(senderId, request.receiverId());
        sendFriendshipRequestUseCase.sendRequest(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlayerDto>> searchPlayers(@AuthenticationPrincipal Jwt jwt, @RequestParam(required = false) String q) {

        UUID loggedInId = UUID.fromString(jwt.getSubject());
        SearchPlayersCommand command = new SearchPlayersCommand(loggedInId, q);

        List<Player> found = searchPlayersUseCase.search(command);

        List<PlayerDto> result = found.stream()
                .map(p -> new PlayerDto(p.getPlayerId().uuid(), p.getUsername(), p.getEmail(),
                        p.getPictureUrl(), p.getCreatedAt())).toList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID friendId) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        RemoveFriendCommand command = new RemoveFriendCommand(playerId, friendId);
        removeFriendUseCase.removeFriend(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playerId}/library/{gameId}")
    public ResponseEntity<GameLibraryDetailsDto> loadOtherPlayerGameDetails(@PathVariable UUID playerId, @PathVariable UUID gameId) {
        LoadSingleGameLibraryCommand command = new LoadSingleGameLibraryCommand(playerId, gameId);

        GameLibrary gl = loadSingleGameLibraryUseCase.loadGame(command);

        GameLibraryDetailsDto result =  new GameLibraryDetailsDto(gl.getGameLibraryId().uuid(),
                gl.getPurchasedAt(), gl.getLastPlayedAt(), gl.getTotalPlaytime().toMinutes(),
                gl.isFavourite(),gl.getMatchesPlayed(), gl.getGamesWon(), gl.getGamesLost(), gl.getGamesDraw());
        return ResponseEntity.ok(result);
    }
}
