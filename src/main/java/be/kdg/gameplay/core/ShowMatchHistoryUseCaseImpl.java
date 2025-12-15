package be.kdg.gameplay.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.ShowMatchHistoryUseCase;
import be.kdg.gameplay.port.out.LoadMatchPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowMatchHistoryUseCaseImpl implements ShowMatchHistoryUseCase {

    private final LoadMatchPort loadMatchPort;

    public ShowMatchHistoryUseCaseImpl(LoadMatchPort loadMatchPort) {
        this.loadMatchPort = loadMatchPort;
    }

    @Override
    public List<Match> showHistory(PlayerId playerId) {
        return loadMatchPort.loadMatchesByPlayerId(playerId);
    }
}