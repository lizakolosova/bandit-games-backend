package be.kdg.player.adapter.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.mapper.PlayerJpaMapper;
import be.kdg.player.domain.Player;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayerJpaAdapter implements LoadPlayerPort, UpdatePlayerPort {

    private final PlayerJpaRepository players;

    public PlayerJpaAdapter(PlayerJpaRepository players) {
        this.players = players;
    }

    @Override
    @Transactional
    public Optional<Player> loadById(PlayerId playerId) {
        return players.findById(playerId.uuid())
                .map(PlayerJpaMapper::toDomain);
    }

    @Override
    @Transactional
    public void update(Player player) {
        PlayerJpaEntity entity = PlayerJpaMapper.toEntity(player);
        players.save(entity);
    }
}