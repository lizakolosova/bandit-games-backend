package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.adapter.out.mapper.GameRoomJpaMapper;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.port.out.AddGameRoomPort;
import org.springframework.stereotype.Repository;

@Repository
public class GameRoomJpaAdapter implements AddGameRoomPort {

    private final GameRoomJpaRepository games;

    public GameRoomJpaAdapter(GameRoomJpaRepository games) {
        this.games = games;
    }

    @Override
    public GameRoom add(GameRoom room) {
        var saved = games.save(GameRoomJpaMapper.toEntity(room));
        return GameRoomJpaMapper.toDomain(saved);
    }
}

