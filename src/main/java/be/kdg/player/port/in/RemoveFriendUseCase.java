package be.kdg.player.port.in;

import be.kdg.player.port.in.command.RemoveFriendCommand;

public interface RemoveFriendUseCase {
    void removeFriend(RemoveFriendCommand command);
}
