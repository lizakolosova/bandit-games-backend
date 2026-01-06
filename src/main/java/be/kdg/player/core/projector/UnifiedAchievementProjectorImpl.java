package be.kdg.player.core.projector;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.AchievementProjection;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.UnifiedAchievementProjector;
import be.kdg.player.port.in.command.UnifiedAchievementUnlockedProjectionCommand;
import be.kdg.player.port.out.LoadAchievementProjectionPort;
import be.kdg.player.port.out.LoadGameProjectionPort;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UnifiedAchievementProjectorImpl implements UnifiedAchievementProjector {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedAchievementProjectorImpl.class);

    private final UpdatePlayerPort updatePlayerPort;
    private final LoadPlayerPort loadPlayerPort;
    private final LoadAchievementProjectionPort loadAchievementProjectionPort;

    public UnifiedAchievementProjectorImpl(UpdatePlayerPort updatePlayerPort, LoadPlayerPort loadPlayerPort,
                                           LoadAchievementProjectionPort loadAchievementProjectionPort,
                                           LoadGameProjectionPort loadGameProjectionPort) {
        this.updatePlayerPort = updatePlayerPort;
        this.loadPlayerPort = loadPlayerPort;
        this.loadAchievementProjectionPort = loadAchievementProjectionPort;
    }

    @Override
    public void projectAchievementUnlocked(UnifiedAchievementUnlockedProjectionCommand command) {


        logger.info("Projecting unified achievement unlocked for player {} in game type {} this {} and this {}",
                command.playerId(), command.gameType(), command.gameId(), command.achievementId());

        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId()))
                .orElseThrow(() -> NotFoundException.player(command.playerId()));

        AchievementProjectionDto achievementProjectionDto = resolveAchievementId(command);

        logger.info("Lepic {} ", command.gameId());


        player.unlockAchievement(AchievementId.of(achievementProjectionDto.achievementId()), GameId.of(achievementProjectionDto.gameId()));

        updatePlayerPort.update(player);
    }

    private AchievementProjectionDto resolveAchievementId(UnifiedAchievementUnlockedProjectionCommand command) {
        if (command.achievementId() != null) {
            return new AchievementProjectionDto(command.achievementId(), command.gameId());
        }

        AchievementProjection projection = loadAchievementProjectionPort
                .loadByGameIdAndType(command.achievementType()).orElseThrow();

        return new AchievementProjectionDto(projection.getAchievementId().uuid(), projection.getGameId().uuid());
    }
}
