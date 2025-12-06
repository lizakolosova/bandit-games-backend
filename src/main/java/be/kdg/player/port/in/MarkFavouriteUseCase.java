package be.kdg.player.port.in;

import be.kdg.player.port.in.command.MarkFavouriteCommand;

public interface MarkFavouriteUseCase {
    void markFavourite(MarkFavouriteCommand command);
}