package be.kdg.platform.adapter.out;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.adapter.out.mapper.GameMapper;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.out.AddGamePort;
import be.kdg.platform.port.out.LoadGamePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GameJpaAdapter implements LoadGamePort, AddGamePort {

    private final GameJpaRepository games;

    public GameJpaAdapter(GameJpaRepository games) {
        this.games = games;
    }

    @Override
    public Optional<Game> loadById(GameId id) {
        return this.games.findById(id.uuid())
                .map(GameMapper::toDomain);
    }

    @Override
    public List<Game> loadAll() {
        return this.games.findAll().stream()
                .map(GameMapper::toDomain)
                .toList();
    }

    @Override
    public Game add(Game game) {
        GameJpaEntity entity = GameMapper.toEntity(game);
        GameJpaEntity saved = games.save(entity);
        return GameMapper.toDomain(saved);
    }
}