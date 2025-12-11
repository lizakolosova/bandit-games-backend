package be.kdg.gameplay.core.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.GameplayEventPublisher;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.gameplay.port.in.UnifiedMatchProjector;
import be.kdg.gameplay.port.in.command.*;
import be.kdg.gameplay.port.out.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UnifiedMatchProjectorImpl implements UnifiedMatchProjector {

    private static final Logger log = LoggerFactory.getLogger(UnifiedMatchProjectorImpl.class);

    private final LoadMatchPort loadMatchPort;
    private final UpdateMatchPort updateMatchPort;
    private final AddMatchPort addMatchPort;
    private final GameplayEventPublisher eventPublisher;

    public UnifiedMatchProjectorImpl(LoadMatchPort loadMatchPort, UpdateMatchPort updateMatchPort, AddMatchPort addMatchPort, GameplayEventPublisher eventPublisher) {
        this.loadMatchPort = loadMatchPort;
        this.updateMatchPort = updateMatchPort;
        this.addMatchPort = addMatchPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void project(UnifiedMatchCreatedProjectionCommand command) {
        log.info("Projecting unified match created: {} for game type: {}",
                command.matchId(), command.gameType());

        Match match = new Match(
                MatchId.of(command.matchId()),
                GameId.of(command.gameId()),
                command.playerIds().stream()
                        .map(PlayerId::of)
                        .collect(Collectors.toList()),
                MatchStatus.IN_PROGRESS,
                command.timestamp()
        );

        addMatchPort.add(match);
    }

    @Override
    public void project(UnifiedMatchUpdatedProjectionCommand command) {
        log.info("Projecting unified match updated: {}", command.matchId());

        Match match = loadMatchPort.loadById(MatchId.of(command.matchId()));

        match.updatePlayers(
                PlayerId.of(command.playerId1()),
                PlayerId.of(command.playerId2())
        );

        updateMatchPort.update(match);
    }

    @Override
    public void project(UnifiedMatchEndedProjectionCommand command) {
        log.info("Projecting unified match ended: {} with winner: {}",
                command.matchId(), command.winnerId());

        Match match = loadMatchPort.loadById(MatchId.of(command.matchId()));

        PlayerId winner = command.winnerId() != null ? PlayerId.of(command.winnerId()) : null;
        match.finish(command.timestamp(), winner);

        updateMatchPort.update(match);
        eventPublisher.publishEvents(match.pullDomainEvents());
    }
}
