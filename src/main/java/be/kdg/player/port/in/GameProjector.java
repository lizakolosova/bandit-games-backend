package be.kdg.player.port.in;

public interface GameProjector {
    void project(GameAddedProjectionCommand command);
}