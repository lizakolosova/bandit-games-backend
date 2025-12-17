package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.out.FriendshipRequestJpaEntity;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.Player;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.port.in.LoadPendingFriendshipRequestUseCase;
import be.kdg.player.port.in.command.PendingFriendshipRequestView;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.LoadPlayerPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoadPendingFriendshipRequestUseCaseImpl implements LoadPendingFriendshipRequestUseCase {

    private final LoadFriendshipRequestPort loadRequestPort;
    private final LoadPlayerPort loadPlayerPort;

    public LoadPendingFriendshipRequestUseCaseImpl(LoadFriendshipRequestPort loadRequestPort, LoadPlayerPort loadPlayerPort) {
        this.loadRequestPort = loadRequestPort;
        this.loadPlayerPort = loadPlayerPort;
    }

    @Override
    public List<PendingFriendshipRequestView> getPendingRequests(UUID receiverId) {
        List<FriendshipRequest> requests = loadRequestPort.findPendingByReceiverId(ReceiverId.of(receiverId));

        return requests.stream()
                .map(req -> {
                    Player sender = loadPlayerPort.loadById(PlayerId.of(req.getSenderId().uuid()))
                            .orElseThrow(()-> NotFoundException.player(req.getSenderId().uuid()));
                    return new PendingFriendshipRequestView(
                            req,
                            sender.getUsername(),
                            sender.getPictureUrl()
                    );
                })
                .toList();
    }
}
