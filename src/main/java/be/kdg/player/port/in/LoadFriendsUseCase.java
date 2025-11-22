package be.kdg.player.port.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.response.FriendDto;

import java.util.List;

public interface LoadFriendsUseCase {
    List<FriendDto> loadFriends(PlayerId playerId);
}
