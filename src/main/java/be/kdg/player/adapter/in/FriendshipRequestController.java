package be.kdg.player.adapter.in;

import be.kdg.player.adapter.in.request.AcceptFriendshipRequest;
import be.kdg.player.adapter.in.request.RejectFriendshipRequest;
import be.kdg.player.adapter.in.response.FriendshipRequestDto;
import be.kdg.player.port.in.AcceptFriendshipRequestCommand;
import be.kdg.player.port.in.AcceptFriendshipRequestUseCase;
import be.kdg.player.port.in.RejectFriendshipRequestCommand;
import be.kdg.player.port.in.RejectFriendshipRequestUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/friendship-requests")
public class FriendshipRequestController {

    private final RejectFriendshipRequestUseCase rejectFriendshipRequestUseCase;
    private final AcceptFriendshipRequestUseCase acceptFriendshipRequestUseCase;

    public FriendshipRequestController(RejectFriendshipRequestUseCase rejectFriendshipRequestUseCase, AcceptFriendshipRequestUseCase acceptFriendshipRequestUseCase) {
        this.rejectFriendshipRequestUseCase = rejectFriendshipRequestUseCase;
        this.acceptFriendshipRequestUseCase = acceptFriendshipRequestUseCase;
    }

    @PostMapping("/reject")
    public ResponseEntity<FriendshipRequestDto> reject(@RequestBody RejectFriendshipRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID receiverId = UUID.fromString(jwt.getSubject());
        RejectFriendshipRequestCommand command = new RejectFriendshipRequestCommand(request.friendshipRequestId(), receiverId);

        return ResponseEntity.ok(FriendshipRequestDto.from(rejectFriendshipRequestUseCase.reject(command)));
    }

    @PostMapping("/accept")
    public ResponseEntity<FriendshipRequestDto> accept(@RequestBody AcceptFriendshipRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID receiverId = UUID.fromString(jwt.getSubject());
        AcceptFriendshipRequestCommand command = new AcceptFriendshipRequestCommand(request.friendshipRequestId(), receiverId);

        return ResponseEntity.ok(FriendshipRequestDto.from( acceptFriendshipRequestUseCase.accept(command)));
    }
}

