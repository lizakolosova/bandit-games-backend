package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.port.in.RetrieveLatestMatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final RetrieveLatestMatchUseCase retrieveLatestMatchUseCase;

    public MatchController(RetrieveLatestMatchUseCase retrieveLatestMatchUseCase) {
        this.retrieveLatestMatchUseCase = retrieveLatestMatchUseCase;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<UUID> getLatestMatchByPlayer(@PathVariable UUID playerId) {
        Optional<UUID> latestMatchId = retrieveLatestMatchUseCase.retrieveLatestMatchIdByPlayer(playerId);

        return latestMatchId
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
