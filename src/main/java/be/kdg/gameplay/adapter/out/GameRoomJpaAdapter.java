package be.kdg.gameplay.adapter.out;

import be.kdg.common.exception.NotFoundException;
import be.kdg.gameplay.adapter.out.mapper.GameRoomJpaMapper;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;
import be.kdg.gameplay.port.out.AddGameRoomPort;
import be.kdg.gameplay.port.out.LoadGameRoomPort;
import be.kdg.gameplay.port.out.UpdateGameRoomPort;
import org.springframework.stereotype.Repository;

@Repository
public class GameRoomJpaAdapter implements AddGameRoomPort, LoadGameRoomPort, UpdateGameRoomPort {

    private final GameRoomJpaRepository games;

    public GameRoomJpaAdapter(GameRoomJpaRepository games) {
        this.games = games;
    }

    @Override
    public GameRoom add(GameRoom room) {
        GameRoomJpaEntity entity = games.save(GameRoomJpaMapper.toEntity(room));
        return GameRoomJpaMapper.toDomain(entity);
    }
    @Override
    public GameRoom loadById(GameRoomId gameRoomId) {
        GameRoomJpaEntity entity = games.findById(gameRoomId.uuid())
                .orElseThrow(() -> new NotFoundException("GameRoom not found: " + gameRoomId.uuid()));

        return GameRoomJpaMapper.toDomain(entity);
    }

    @Override
    public GameRoom update(GameRoom gameRoom) {
        GameRoomJpaEntity entity = games.save(GameRoomJpaMapper.toEntity(gameRoom));
        return GameRoomJpaMapper.toDomain(entity);
    }
}

