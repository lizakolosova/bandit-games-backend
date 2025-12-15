package be.kdg.player.port.in;
import be.kdg.player.port.in.command.UpdatePlayerStatisticsCommand;

public interface PlayerStatisticsProjector {
    void project(UpdatePlayerStatisticsCommand command);
}
