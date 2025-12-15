package be.kdg.player.core;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.port.in.PendingFriendshipRequestUseCase;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PendingFriendshipRequestUseCaseImpl implements PendingFriendshipRequestUseCase {

    private final LoadFriendshipRequestPort loadPort;

    public PendingFriendshipRequestUseCaseImpl(LoadFriendshipRequestPort loadPort) {
        this.loadPort = loadPort;
    }

    @Override
    public List<FriendshipRequest> getPendingRequests(UUID receiverId) {
        return loadPort.findPendingByReceiverId(ReceiverId.of(receiverId));
    }
}
