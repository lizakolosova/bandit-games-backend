package be.kdg.player.core.projector;

import be.kdg.common.exception.NotFoundException;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.AchievementProjector;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;

import be.kdg.player.port.in.command.ChessAchievementUnlockedProjectionCommand;
import be.kdg.player.port.in.command.TicTacToeAchievementUnlockedProjectionCommand;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;


@Component
public class AchievementProjectorImpl implements AchievementProjector {

    private static final Logger logger = LoggerFactory.getLogger(AchievementProjectorImpl.class);

    private final UpdatePlayerPort updatePlayerPort;
    private final LoadPlayerPort loadPlayerPort;

    public AchievementProjectorImpl(UpdatePlayerPort updatePlayerPort, LoadPlayerPort loadPlayerPort) {
        this.updatePlayerPort = updatePlayerPort;
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    public void project(TicTacToeAchievementUnlockedProjectionCommand command) {
        logger.info("Projecting achievement unlocked: {}", command);

        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId()))
                .orElseThrow(() -> NotFoundException.player(command.playerId()));

        player.unlockAchievement(AchievementId.of(command.achievementId()),
                GameId.of(command.gameId()));

        updatePlayerPort.update(player);
    }
    @Override
    public void project(ChessAchievementUnlockedProjectionCommand command) {
        logger.info("Projecting Chess achievement unlock for player {}", command.playerId());

        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId()))
                .orElseThrow(() -> NotFoundException.player(command.playerId()));

        player.unlockAchievement(AchievementId.of(command.achievementId()), GameId.of(command.gameId()));

        updatePlayerPort.update(player);
    }
}
