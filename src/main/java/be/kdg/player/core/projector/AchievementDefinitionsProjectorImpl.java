package be.kdg.player.core.projector;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.AchievementProjection;
import be.kdg.player.port.in.AchievementDefinitionProjector;
import be.kdg.player.port.in.command.AchievementAddedProjectionCommand;
import be.kdg.player.port.out.AddAchievementProjectionPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AchievementDefinitionsProjectorImpl implements AchievementDefinitionProjector {

    private final AddAchievementProjectionPort achievements;

    public AchievementDefinitionsProjectorImpl(AddAchievementProjectionPort achievements) {
        this.achievements = achievements;
    }

    @Override
    public void project(AchievementAddedProjectionCommand command) {
        AchievementProjection projection = new AchievementProjection(
                AchievementId.of(UUID.fromString(command.achievementId())),
                command.name(),
                command.description(),
                GameId.of(UUID.fromString(command.gameId()))
        );

        achievements.add(projection);
    }
}
