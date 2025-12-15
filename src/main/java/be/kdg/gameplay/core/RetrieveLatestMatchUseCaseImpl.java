package be.kdg.gameplay.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.in.RetrieveLatestMatchUseCase;
import be.kdg.gameplay.port.out.LoadMatchPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RetrieveLatestMatchUseCaseImpl implements RetrieveLatestMatchUseCase {

    private final LoadMatchPort loadMatchPort;

    public RetrieveLatestMatchUseCaseImpl(LoadMatchPort loadMatchPort) {
        this.loadMatchPort = loadMatchPort;
    }

    @Override
    public Match LoadLatestMatchByPlayer(PlayerId playerId) {
        return loadMatchPort.loadByPlayerId(playerId);
    }
}
