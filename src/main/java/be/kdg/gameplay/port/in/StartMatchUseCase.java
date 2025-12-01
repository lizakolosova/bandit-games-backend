package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.Match;

public interface StartMatchUseCase {
    Match start(StartMatchCommand command);
}
