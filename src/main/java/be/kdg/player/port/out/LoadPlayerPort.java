package be.kdg.player.port.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;

import java.util.Optional;

public interface LoadPlayerPort {
    Optional<Player> loadById(PlayerId playerId);
}

