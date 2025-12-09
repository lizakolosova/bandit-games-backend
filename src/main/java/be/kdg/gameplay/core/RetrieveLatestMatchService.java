package be.kdg.gameplay.core;

import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.RetrieveLatestMatchUseCase;
import be.kdg.gameplay.port.out.LoadMatchPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Service
public class RetrieveLatestMatchService implements RetrieveLatestMatchUseCase {

    private final LoadMatchPort loadMatchPort;

    public RetrieveLatestMatchService(LoadMatchPort loadMatchPort) {
        this.loadMatchPort = loadMatchPort;
    }

    @Override
    public Optional<UUID> retrieveLatestMatchIdByPlayer(UUID playerId) {

        List<Match> matches = loadMatchPort.loadByPlayerId(playerId);

        if (matches.isEmpty()) {
            return Optional.empty();
        }

        return matches.stream()
                .max(Comparator.comparing(Match::getStartedAt))
                .map(match -> match.getMatchId().uuid());
    }
}
