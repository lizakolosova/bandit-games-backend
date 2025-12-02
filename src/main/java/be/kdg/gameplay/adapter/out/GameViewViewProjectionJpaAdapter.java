package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.adapter.out.mapper.GameViewProjectionMapper;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.port.out.AddGameViewProjectionPort;
import be.kdg.gameplay.port.out.LoadGameViewProjectionPort;
import org.springframework.stereotype.Component;

@Component
public class GameViewViewProjectionJpaAdapter implements AddGameViewProjectionPort, LoadGameViewProjectionPort {

    private final GameViewProjectionJpaRepository repository;

    public GameViewViewProjectionJpaAdapter(GameViewProjectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(GameViewProjection projection) {
        repository.save(GameViewProjectionMapper.toEntity(projection));
    }

    @Override
    public GameViewProjection findByName(String name) {
        return repository.findByName(name)
                .map(GameViewProjectionMapper::toDomain)
                .orElseThrow(() -> new IllegalStateException("Game projection not found: " + name));
    }
}

