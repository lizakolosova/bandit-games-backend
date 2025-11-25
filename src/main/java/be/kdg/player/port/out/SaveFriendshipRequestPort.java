package be.kdg.player.port.out;

import be.kdg.player.domain.FriendshipRequest;

public interface SaveFriendshipRequestPort {
    void save(FriendshipRequest request);
}