package be.kdg.player.port.in;

import be.kdg.player.port.in.command.FriendRemovedProjectionCommand;

public interface FriendRemovedProjector {
    void project(FriendRemovedProjectionCommand command);
}

