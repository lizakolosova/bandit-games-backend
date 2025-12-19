package be.kdg.player.port.in;

import be.kdg.player.port.in.command.FriendshipRequestAcceptedProjectionCommand;

public interface  FriendshipRequestAcceptedProjector {
    void project(FriendshipRequestAcceptedProjectionCommand command);
}
