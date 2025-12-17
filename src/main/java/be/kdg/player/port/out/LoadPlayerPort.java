package be.kdg.player.port.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface LoadPlayerPort {
    Optional<Player> loadById(PlayerId playerId);
    List<Player> searchExcluding(PlayerId excludeId, String name);
    Map<PlayerId, Player> loadByIds(Set<PlayerId> ids);
}

