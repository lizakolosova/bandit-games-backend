package be.kdg.player.port.in;

import be.kdg.player.adapter.in.response.LibraryGameDetailsDto;
import be.kdg.player.port.in.command.LoadLibraryGameCommand;

public interface LoadLibraryGameUseCase {
    LibraryGameDetailsDto loadGame(LoadLibraryGameCommand command);
}