package be.kdg.player.adapter.out;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.port.out.AddGameProjectionPort;
import be.kdg.player.port.out.LoadGameProjectionPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class GameProjectionJpaAdapter implements LoadGameProjectionPort, AddGameProjectionPort {

    private final GameProjectionJpaRepository games;

    public GameProjectionJpaAdapter(GameProjectionJpaRepository games) {
        this.games = games;
    }

    @Override
    public GameProjection loadProjection(GameId gameId) {
        GameProjectionJpaEntity entity = games.findById(gameId.uuid())
                .orElseThrow(() -> NotFoundException.game(gameId.uuid()));

        return new GameProjection(entity.getGameId(), entity.getName(), entity.getPictureUrl(), entity.getCategory(),
                entity.getRules(), entity.getAchievementCount(), entity.getAverageMinutes(), entity.getDevelopedBy());
    }
    @Override
    public void addGameProjection(GameProjection gameProjection) {
        games.save(new GameProjectionJpaEntity(gameProjection.getGameId(), gameProjection.getName(),
        gameProjection.getPictureUrl(), gameProjection.getCategory(), gameProjection.getRules(), gameProjection.getAchievementCount(),
                gameProjection.getAverageMinutes(), gameProjection.getDevelopedBy()));
    }
}
