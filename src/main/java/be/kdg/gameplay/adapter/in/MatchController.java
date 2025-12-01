package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.SoloMatchRequest;
import be.kdg.gameplay.adapter.in.response.SoloMatchDto;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.CreateSoloMatchCommand;
import be.kdg.gameplay.port.in.CreateSoloMatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/gameplay")
public class MatchController {

    private final CreateSoloMatchUseCase createSoloMatchUseCase;

    public MatchController(CreateSoloMatchUseCase createSoloMatchUseCase) {
        this.createSoloMatchUseCase = createSoloMatchUseCase;
    }

    @PostMapping("/matches/solo")
    public ResponseEntity<SoloMatchDto> createSoloMatch(@AuthenticationPrincipal Jwt jwt, @RequestBody SoloMatchRequest request) {
        UUID playerId = UUID.fromString(jwt.getSubject());
        CreateSoloMatchCommand command = new CreateSoloMatchCommand(playerId, request.gameId());
        Match match = createSoloMatchUseCase.createSoloMatch(command);

        return ResponseEntity.ok(new SoloMatchDto(match.getMatchId().uuid(), match.getGameId().uuid(), match.getStatus().toString()));
    }
}