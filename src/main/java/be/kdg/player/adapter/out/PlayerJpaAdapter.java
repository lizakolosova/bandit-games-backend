package be.kdg.player.adapter.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.mapper.PlayerJpaMapper;
import be.kdg.player.domain.Player;
import be.kdg.player.port.out.LoadPlayerPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayerJpaAdapter implements LoadPlayerPort {

    private final PlayerJpaRepository players;
    private final PlayerJpaMapper mapper = new PlayerJpaMapper();

    public PlayerJpaAdapter(PlayerJpaRepository players) {
        this.players = players;
    }

    @Override
    public Optional<Player> loadById(PlayerId playerId) {
        return players.findById(playerId.uuid())
                .map(mapper::toDomain);
    }
}