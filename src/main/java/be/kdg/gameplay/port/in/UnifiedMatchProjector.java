package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.*;

public interface UnifiedMatchProjector {
    void project(UnifiedMatchCreatedProjectionCommand command);
    void project(UnifiedMatchUpdatedProjectionCommand command);
    void project(UnifiedMatchEndedProjectionCommand command);
}
