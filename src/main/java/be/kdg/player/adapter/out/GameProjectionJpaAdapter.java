package be.kdg.player.adapter.out;

import be.kdg.common.exception.NotFoundException;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.port.out.LoadGameProjectionPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameProjectionJpaAdapter implements LoadGameProjectionPort {

    private final GameProjectionJpaRepository games;

    public GameProjectionJpaAdapter(GameProjectionJpaRepository games) {
        this.games = games;
    }

    @Override
    public GameProjection loadProjection(UUID gameId) {
        var entity = games.findById(gameId)
                .orElseThrow(() -> NotFoundException.game(gameId));

        return new GameProjection(
                entity.getGameId(),
                entity.getName(),
                entity.getPictureUrl(),
                entity.getCategory(),
                entity.getRules(),
                entity.getAchievementCount(),
                entity.getAverageMinutes(),
                entity.getDevelopedBy()
        );
    }
}
