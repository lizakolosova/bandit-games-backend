package be.kdg.player.core;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.port.in.command.RejectFriendshipRequestCommand;
import be.kdg.player.port.in.RejectFriendshipRequestUseCase;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.UpdateFriendshipRequestPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RejectFriendshipRequestUseCaseImpl implements RejectFriendshipRequestUseCase {

    private final LoadFriendshipRequestPort loadFriendshipRequestPort;
    private final UpdateFriendshipRequestPort UpdateFriendshipRequestPort;

    public RejectFriendshipRequestUseCaseImpl(LoadFriendshipRequestPort loadFriendshipRequestPort, UpdateFriendshipRequestPort updateFriendshipRequestPort) {
        this.loadFriendshipRequestPort = loadFriendshipRequestPort;
        UpdateFriendshipRequestPort = updateFriendshipRequestPort;
    }

    @Override
    @Transactional
    public FriendshipRequest reject(RejectFriendshipRequestCommand command) {

        FriendshipRequest friendshipRequest = loadFriendshipRequestPort.load(FriendshipRequestId.of(command.friendshipRequestId()));

        if (!friendshipRequest.getReceiverId().uuid().equals(command.receiverId())) {
            throw new IllegalStateException("Only the receiver can reject the friendship.");
        }

        friendshipRequest.reject();
        return UpdateFriendshipRequestPort.update(friendshipRequest);
    }
}