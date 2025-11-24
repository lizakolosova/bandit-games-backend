package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.LoadFriendsUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LoadFriendsUseCaseImpl implements LoadFriendsUseCase {

    private final LoadPlayerPort loadPlayerPort;

    public LoadFriendsUseCaseImpl(LoadPlayerPort loadPlayerPort) {
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    public List<Player> loadFriends(PlayerId playerId) {

        Player player = loadPlayerPort.loadById(playerId).orElseThrow(() -> NotFoundException.player(playerId.uuid()));

        return player.getFriends().stream()
                .map(friend -> {
                    Player friendPlayer =
                            loadPlayerPort.loadById(PlayerId.of(friend.friendId())).orElseThrow(()-> NotFoundException.player(friend.friendId()));

                    return new Player(
                            PlayerId.of(friend.friendId()),
                            friendPlayer.getUsername(),
                            friendPlayer.getEmail(),
                            friendPlayer.getPictureUrl(),
                            friendPlayer.getCreatedAt()
                    );
                })
                .toList();
    }
}

