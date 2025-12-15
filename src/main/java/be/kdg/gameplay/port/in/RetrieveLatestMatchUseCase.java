package be.kdg.gameplay.port.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;

import java.util.UUID;

public interface RetrieveLatestMatchUseCase {
    Match LoadLatestMatchByPlayer(PlayerId playerId);
}
