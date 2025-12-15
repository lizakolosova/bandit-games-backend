package be.kdg.platform.port.in;

import be.kdg.platform.port.in.command.RegisterChessGameProjectionCommand;

public interface ChessPlatformProjector {
    void project(RegisterChessGameProjectionCommand command);
}
