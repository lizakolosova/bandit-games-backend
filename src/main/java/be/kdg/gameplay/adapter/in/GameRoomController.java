package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.CreateGameRoomRequest;
import be.kdg.gameplay.adapter.in.response.GameRoomDto;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.*;
import be.kdg.gameplay.port.in.command.AcceptInvitationCommand;
import be.kdg.gameplay.port.in.command.CreateGameRoomCommand;
import be.kdg.gameplay.port.in.command.RejectInvitationCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/game-rooms")
public class GameRoomController {

    private final CreateGameRoomUseCase createGameRoomUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;
    private final RejectInvitationUseCase rejectInvitationUseCase;

    public GameRoomController(CreateGameRoomUseCase createGameRoomUseCase, AcceptInvitationUseCase acceptInvitationUseCase, RejectInvitationUseCase rejectInvitationUseCase) {
        this.createGameRoomUseCase = createGameRoomUseCase;
        this.acceptInvitationUseCase = acceptInvitationUseCase;
        this.rejectInvitationUseCase = rejectInvitationUseCase;
    }

    @PostMapping
    public ResponseEntity<GameRoomDto> create(@RequestBody CreateGameRoomRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        CreateGameRoomCommand command = new CreateGameRoomCommand(request.gameId(), playerId,
                request.invitedPlayerId() == null ? null : request.invitedPlayerId(), GameRoomType.valueOf(request.gameRoomType()));

        return ResponseEntity.ok(GameRoomDto.from(createGameRoomUseCase.create(command)));
    }

    @PostMapping("/{roomId}/accept")
    public ResponseEntity<Void> accept(@PathVariable UUID roomId, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        acceptInvitationUseCase.accept(new AcceptInvitationCommand(roomId, playerId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID roomId, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        rejectInvitationUseCase.reject(new RejectInvitationCommand(roomId, playerId));
        return ResponseEntity.ok().build();
    }
}

