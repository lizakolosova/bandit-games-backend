package be.kdg.player.port.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;

import java.util.List;

public interface LoadFriendsUseCase {
    List<Player> loadFriends(PlayerId playerId);
}
