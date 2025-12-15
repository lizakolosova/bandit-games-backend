package be.kdg.platform.port.in;

import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.command.ApproveGameCommand;

public interface ApproveGameUseCase {
    void approve(ApproveGameCommand command) throws GameNotFoundException;
}