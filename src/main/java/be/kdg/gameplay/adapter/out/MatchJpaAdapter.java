package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.adapter.out.mapper.MatchJpaMapper;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.port.out.AddMatchPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository

public class MatchJpaAdapter implements AddMatchPort {
    private final MatchJpaRepository matches;

    public MatchJpaAdapter(MatchJpaRepository matches) {
        this.matches = matches;
    }

    @Override
    @Transactional
    public void add(Match match) {
        MatchJpaEntity entity = MatchJpaMapper.toEntity(match);
        matches.save(entity);
    }
}
