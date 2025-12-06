package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;

public interface LoadMatchPort {
    Match loadById(MatchId id);
}