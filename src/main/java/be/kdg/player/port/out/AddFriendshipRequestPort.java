package be.kdg.player.port.out;

import be.kdg.player.domain.FriendshipRequest;

public interface AddFriendshipRequestPort {
    void save(FriendshipRequest request);
}