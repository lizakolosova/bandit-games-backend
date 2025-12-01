package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.MatchRequest;
import be.kdg.gameplay.adapter.in.response.MatchDto;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.StartMatchCommand;
import be.kdg.gameplay.port.in.StartMatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameplay")
public class MatchController {

    private final StartMatchUseCase startMatchUseCase;

    public MatchController(StartMatchUseCase startMatchUseCase) {
        this.startMatchUseCase = startMatchUseCase;
    }

    @PostMapping("/matches/start")
    public ResponseEntity<MatchDto> createSoloMatch(@AuthenticationPrincipal Jwt jwt, @RequestBody MatchRequest request) {
        StartMatchCommand command = new StartMatchCommand(request.gameRoomId());
        Match match = startMatchUseCase.start(command);

        return ResponseEntity.ok(new MatchDto(match.getMatchId().uuid(), match.getGameId().uuid(), match.getStatus().toString()));
    }
}