package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;

import java.util.UUID;

public interface LoadMatchPort {
    Match loadById(MatchId id);
    Match loadByPlayerId(UUID playerId);
}