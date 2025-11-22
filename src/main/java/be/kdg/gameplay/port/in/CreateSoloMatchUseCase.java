package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.Match;

public interface CreateSoloMatchUseCase {
    Match createSoloMatch(CreateSoloMatchCommand command);
}
