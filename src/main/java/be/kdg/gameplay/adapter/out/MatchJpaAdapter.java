package be.kdg.gameplay.adapter.out;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.mapper.MatchJpaMapper;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.gameplay.port.out.AddMatchPort;
import be.kdg.gameplay.port.out.LoadMatchPort;
import be.kdg.gameplay.port.out.UpdateMatchPort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchJpaAdapter implements AddMatchPort, UpdateMatchPort, LoadMatchPort {
    private final MatchJpaRepository matches;

    public MatchJpaAdapter(MatchJpaRepository matches) {
        this.matches = matches;
    }

    @Override
    @Transactional
    public Match add(Match match) {
        MatchJpaEntity entity = MatchJpaMapper.toEntity(match);
        return MatchJpaMapper.toDomain(matches.save(entity));
    }

    @Override
    public Match loadById(MatchId matchId) {
        return matches.findById(matchId.uuid())
                .map(MatchJpaMapper::toDomain).orElseThrow(NotFoundException::new);
    }

    @Override
    public Match update(Match match) {
        matches.save(MatchJpaMapper.toEntity(match));
        return match;
    }
    @Override
    public Match loadByPlayerId(PlayerId playerId) {
        return  matches.findFirstByPlayersContainingOrderByStartedAtDesc(playerId.uuid())
                .map(MatchJpaMapper::toDomain).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Match> loadMatchesByPlayerId(PlayerId playerId) {
        return matches.findAllByPlayerId(playerId.uuid()).stream().map(MatchJpaMapper::toDomain).toList();
    }

}