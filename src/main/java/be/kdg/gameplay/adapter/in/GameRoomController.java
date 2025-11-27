package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.CreateGameRoomRequest;
import be.kdg.gameplay.adapter.in.response.GameRoomDto;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.CreateGameRoomCommand;
import be.kdg.gameplay.port.in.CreateGameRoomUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/game-rooms")
public class GameRoomController {

    private final CreateGameRoomUseCase createGameRoomUseCase;

    public GameRoomController(CreateGameRoomUseCase createGameRoomUseCase) {
        this.createGameRoomUseCase = createGameRoomUseCase;
    }

    @PostMapping
    public ResponseEntity<GameRoomDto> create(@RequestBody CreateGameRoomRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        CreateGameRoomCommand command = new CreateGameRoomCommand(request.gameId(), playerId,
                request.invitedPlayerId() == null ? null : request.invitedPlayerId(), GameRoomType.valueOf(request.gameRoomType()));

        return ResponseEntity.ok(GameRoomDto.from(createGameRoomUseCase.create(command)));
    }
}

