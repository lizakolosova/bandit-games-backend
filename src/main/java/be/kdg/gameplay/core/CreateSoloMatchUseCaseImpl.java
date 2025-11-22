package be.kdg.gameplay.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.AIPlayer;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.port.in.CreateSoloMatchCommand;
import be.kdg.gameplay.port.in.CreateSoloMatchUseCase;

import be.kdg.gameplay.port.out.AddMatchPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreateSoloMatchUseCaseImpl implements CreateSoloMatchUseCase {

    private final AddMatchPort addMatchPort;

    public CreateSoloMatchUseCaseImpl(AddMatchPort addMatchPort) {
        this.addMatchPort = addMatchPort;
    }

    @Override
    public Match createSoloMatch(CreateSoloMatchCommand command) {

        PlayerId human = PlayerId.of(command.playerId());

        Match match = new Match(
                MatchId.create(),
                new GameId(command.gameId()),
                List.of(human, AIPlayer.AI_PLAYER)
        );

        match.start();
        addMatchPort.add(match);
        return match;
    }
}
