package be.kdg.player.port.in;

import be.kdg.player.adapter.in.response.LibraryGameDetailsDto;

public interface LoadLibraryGameUseCase {
    LibraryGameDetailsDto loadGame(LoadLibraryGameCommand command);
}