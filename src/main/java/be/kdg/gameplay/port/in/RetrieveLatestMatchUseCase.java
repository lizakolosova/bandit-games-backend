package be.kdg.gameplay.port.in;

import java.util.Optional;
import java.util.UUID;

public interface RetrieveLatestMatchUseCase {
    Optional<UUID> retrieveLatestMatchIdByPlayer(UUID playerId);
}
