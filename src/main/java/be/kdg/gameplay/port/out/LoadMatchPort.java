package be.kdg.gameplay.port.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;

import java.util.List;
import java.util.UUID;

public interface LoadMatchPort {
    Match loadById(MatchId matchId);
    Match loadByPlayerId(PlayerId playerId);
    List<Match> loadMatchesByPlayerId(PlayerId playerId);
}