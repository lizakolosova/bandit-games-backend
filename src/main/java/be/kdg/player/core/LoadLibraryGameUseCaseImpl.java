package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.response.UnlockedAchievementDto;
import be.kdg.player.domain.GameLibrary;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.port.in.LoadLibraryGameCommand;
import be.kdg.player.port.in.LoadLibraryGameUseCase;
import be.kdg.player.adapter.in.response.LibraryGameDetailsDto;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.LoadGameProjectionPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadLibraryGameUseCaseImpl implements LoadLibraryGameUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final LoadGameProjectionPort loadGameProjectionPort;

    public LoadLibraryGameUseCaseImpl(LoadPlayerPort loadPlayerPort,
                                      LoadGameProjectionPort loadGameProjectionPort) {
        this.loadPlayerPort = loadPlayerPort;
        this.loadGameProjectionPort = loadGameProjectionPort;
    }

    @Override
    public LibraryGameDetailsDto loadGame(LoadLibraryGameCommand command) {

        Player player = loadPlayerPort.loadById(PlayerId.of(command.playerId()))
                .orElseThrow();

        GameLibrary libraryEntry = player.findGameInLibrary(command.gameId());

        GameProjection projection =
                loadGameProjectionPort.loadProjection(command.gameId());

        var unlockedAchievements = player.getAchievements().stream()
                .filter(a -> a.getGameId().uuid().equals(command.gameId()))
                .map(a -> new UnlockedAchievementDto(
                        a.getAchievementId().toString(),   // TODO replace with real title
                        "TODO description",
                        0,                                  // TODO global percentage
                        a.getUnlockedAt()
                ))
                .toList();

        return new LibraryGameDetailsDto(
                projection.getGameId(),

                projection.getName(),
                projection.getPictureUrl(),
                projection.getCategory(),
                projection.getAchievementCount(),
                projection.getAverageMinutes(),
                projection.getDevelopedBy(),

                libraryEntry.getAddedAt(),
                libraryEntry.getLastPlayedAt(),
                libraryEntry.getTotalPlaytime().toMinutes(),
                libraryEntry.isFavourite(),
                unlockedAchievements.size(),
                unlockedAchievements
        );
    }
}