package be.kdg.player.port.out;

import be.kdg.player.domain.FriendshipRequest;

public interface UpdateFriendshipRequestPort {
    FriendshipRequest update(FriendshipRequest friendshipRequest);
}

