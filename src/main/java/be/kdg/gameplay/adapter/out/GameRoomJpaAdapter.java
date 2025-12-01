package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.adapter.out.mapper.GameRoomJpaMapper;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.out.AddGameRoomPort;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import org.springframework.stereotype.Repository;

@Repository
public class GameRoomJpaAdapter implements AddGameRoomPort, UpdateGameRoomPort, LoadGameRoomPort {

    private final GameRoomJpaRepository games;

    public GameRoomJpaAdapter(GameRoomJpaRepository games) {
        this.games = games;
    }

    @Override
    public GameRoom add(GameRoom room) {
        GameRoomJpaEntity saved = games.save(GameRoomJpaMapper.toEntity(room));
        return GameRoomJpaMapper.toDomain(saved);
    }

    @Override
    public GameRoom loadById(GameRoomId id) {
        return games.findById(id.uuid())
                .map(GameRoomJpaMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("GameRoom not found"));
    }

    @Override
    public GameRoom update(GameRoom room) {
        return GameRoomJpaMapper.toDomain(games.save(GameRoomJpaMapper.toEntity(room)));
    }
}