package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.CreateGameRoomRequest;
import be.kdg.gameplay.adapter.in.response.GameRoomDto;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.domain.valueobj.GameRoomType;
import be.kdg.gameplay.port.in.*;
import be.kdg.gameplay.port.in.command.AcceptInvitationCommand;
import be.kdg.gameplay.port.in.command.CreateGameRoomCommand;
import be.kdg.gameplay.port.in.command.RejectInvitationCommand;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
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
    private final FinalizeRoomUseCase finalizeRoomUseCase;
    private final LoadGameRoomPort loadGameRoomPort;

    public GameRoomController(FinalizeRoomUseCase finalizeRoomUseCase, RejectInvitationUseCase rejectInvitationUseCase, AcceptInvitationUseCase acceptInvitationUseCase, CreateGameRoomUseCase createGameRoomUseCase, LoadGameRoomPort loadGameRoomPort) {
        this.finalizeRoomUseCase = finalizeRoomUseCase;
        this.rejectInvitationUseCase = rejectInvitationUseCase;
        this.acceptInvitationUseCase = acceptInvitationUseCase;
        this.createGameRoomUseCase = createGameRoomUseCase;
        this.loadGameRoomPort = loadGameRoomPort;
    }

    @PostMapping
    public ResponseEntity<GameRoomDto> create(@RequestBody CreateGameRoomRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        String name = jwt.getClaimAsString("given_name");
        CreateGameRoomCommand command = new CreateGameRoomCommand(request.gameId(), name, request.invitedPlayerName(), playerId,
                request.invitedPlayerId() == null ? null : request.invitedPlayerId(), GameRoomType.valueOf(request.gameRoomType()));

        return ResponseEntity.ok(GameRoomDto.from(createGameRoomUseCase.create(command)));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<GameRoomDto> getGameRoom(@PathVariable UUID roomId) {
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(roomId));
        return ResponseEntity.ok(GameRoomDto.from(room));
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

    @PostMapping("/{roomId}/finalize")
    public ResponseEntity<GameRoomDto> finalizeRoom(@PathVariable UUID roomId, @AuthenticationPrincipal Jwt jwt) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        FinalizeRoomCommand command = new FinalizeRoomCommand(roomId, playerId);
        GameRoom gameRoom = finalizeRoomUseCase.finalize(command);

        return ResponseEntity.ok(GameRoomDto.from(gameRoom));
    }

    @PostMapping("/{roomId}/accept-ai")
    public ResponseEntity<Void> acceptAsAI(@PathVariable UUID roomId, @AuthenticationPrincipal Jwt jwt) {
        UUID hostPlayerId = UUID.fromString(jwt.getSubject());
        UUID AI_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Verify caller is the host
        GameRoom room = loadGameRoomPort.loadById(GameRoomId.of(roomId));
        if (!room.getHostPlayerId().uuid().equals(hostPlayerId)) {
            return ResponseEntity.status(403).build();
        }

        // Accept as AI player
        acceptInvitationUseCase.accept(new AcceptInvitationCommand(roomId, AI_PLAYER_ID));
        return ResponseEntity.ok().build();
    }


}

