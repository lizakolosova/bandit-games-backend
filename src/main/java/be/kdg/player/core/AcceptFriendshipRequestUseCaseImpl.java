package be.kdg.player.core;

import be.kdg.player.adapter.out.PlayerEventPublisher;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.FriendshipRequestId;
import be.kdg.player.port.in.command.AcceptFriendshipRequestCommand;
import be.kdg.player.port.in.AcceptFriendshipRequestUseCase;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.UpdateFriendshipRequestPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AcceptFriendshipRequestUseCaseImpl implements AcceptFriendshipRequestUseCase {
    private final LoadFriendshipRequestPort loadFriendshipRequestPort;
    private final UpdateFriendshipRequestPort updateFriendshipRequestPort;
    private final PlayerEventPublisher eventPublisher;

    public AcceptFriendshipRequestUseCaseImpl(LoadFriendshipRequestPort loadFriendshipRequestPort, UpdateFriendshipRequestPort updateFriendshipRequestPort, PlayerEventPublisher eventPublisher) {
        this.loadFriendshipRequestPort = loadFriendshipRequestPort;
        this.updateFriendshipRequestPort = updateFriendshipRequestPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public FriendshipRequest accept(AcceptFriendshipRequestCommand command) {
        FriendshipRequest friendshipRequest = loadFriendshipRequestPort.load(FriendshipRequestId.of(command.friendshipRequestId()));

        if (!friendshipRequest.getReceiverId().uuid().equals(command.receiverId())) {
            throw new IllegalStateException("Only the receiver can accept the friendship.");
        }

        friendshipRequest.accept();

        eventPublisher.publishEvents(friendshipRequest.pullDomainEvents());
        return updateFriendshipRequestPort.update(friendshipRequest);
    }
}
