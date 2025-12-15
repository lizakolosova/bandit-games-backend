package be.kdg.gameplay.port.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;

import java.util.List;

public interface ShowMatchHistoryUseCase {
    List<Match> showHistory(PlayerId playerId);
}

