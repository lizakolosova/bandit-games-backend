package be.kdg.gameplay.adapter.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.in.response.MatchHistoryDto;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.RetrieveLatestMatchUseCase;
import be.kdg.gameplay.port.in.ShowMatchHistoryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final RetrieveLatestMatchUseCase retrieveLatestMatchUseCase;
    private final ShowMatchHistoryUseCase showMatchHistoryUseCase;

    public MatchController(RetrieveLatestMatchUseCase retrieveLatestMatchUseCase,  ShowMatchHistoryUseCase showMatchHistoryUseCase) {
        this.retrieveLatestMatchUseCase = retrieveLatestMatchUseCase;
        this.showMatchHistoryUseCase = showMatchHistoryUseCase;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<UUID> getLatestMatchByPlayer(@PathVariable UUID playerId) {
        Match latestMatch = retrieveLatestMatchUseCase.LoadLatestMatchByPlayer(PlayerId.of(playerId));

        return ResponseEntity.ok(latestMatch.getMatchId().uuid());
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<MatchHistoryDto>> getMatchHistory(@PathVariable UUID playerId) {

        List<Match> matches = showMatchHistoryUseCase.showHistory(PlayerId.of(playerId));

        return ResponseEntity.ok(matches.stream().map(MatchHistoryDto::from).toList());
    }
}
