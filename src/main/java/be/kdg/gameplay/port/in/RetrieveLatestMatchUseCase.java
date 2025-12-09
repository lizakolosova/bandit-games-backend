package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.Match;

import java.util.UUID;

public interface RetrieveLatestMatchUseCase {
    Match LoadLatestMatchByPlayer(UUID playerId);
}
