package be.kdg.player.core;

import be.kdg.common.exception.NotFoundException;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.domain.valueobj.ReceiverId;
import be.kdg.player.domain.valueobj.SenderId;
import be.kdg.player.port.in.SendFriendshipRequestCommand;
import be.kdg.player.port.in.SendFriendshipRequestUseCase;
import be.kdg.player.port.out.LoadFriendshipRequestPort;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.AddFriendshipRequestPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SendFriendshipRequestUseCaseImpl implements SendFriendshipRequestUseCase {

    private final LoadPlayerPort loadPlayerPort;
    private final AddFriendshipRequestPort addFriendshipRequestPort;
    private final LoadFriendshipRequestPort loadFriendshipRequestPort;

    public SendFriendshipRequestUseCaseImpl(
            LoadPlayerPort loadPlayerPort,
            AddFriendshipRequestPort addFriendshipRequestPort,
            LoadFriendshipRequestPort loadFriendshipRequestPort
    ) {
        this.loadPlayerPort = loadPlayerPort;
        this.addFriendshipRequestPort = addFriendshipRequestPort;
        this.loadFriendshipRequestPort = loadFriendshipRequestPort;
    }

    @Override
    public FriendshipRequest sendRequest(SendFriendshipRequestCommand command) {
        loadPlayerPort.loadById(PlayerId.of(command.senderId()))
                .orElseThrow(() -> new NotFoundException("Sender not found"));

        loadPlayerPort.loadById(PlayerId.of(command.receiverId()))
                .orElseThrow(() -> new NotFoundException("Receiver not found"));


        if (loadFriendshipRequestPort.existsPending(SenderId.of(command.senderId()), ReceiverId.of(command.receiverId()))) {
            throw new IllegalStateException("Request already exists.");
        }
        FriendshipRequest request = new FriendshipRequest(
                new SenderId(command.senderId()),
                new ReceiverId(command.receiverId())
        );
        addFriendshipRequestPort.save(request);
        return request;
    }
}