package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.Match;

public interface UpdateMatchPort {
    Match update(Match match);
}
