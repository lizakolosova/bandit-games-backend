package be.kdg.player.adapter.in.response;

import be.kdg.player.domain.FriendshipRequest;
import be.kdg.player.port.in.command.PendingFriendshipRequestView;

public record FriendshipRequestWithReceiverDto(
        String friendshipRequestId,
        String senderId,
        String senderUsername,
        String senderPictureUrl,
        String receiverId,
        String status
) {
    public static FriendshipRequestWithReceiverDto from(PendingFriendshipRequestView v) {
        FriendshipRequest f = v.request();
        return new FriendshipRequestWithReceiverDto(
                f.getFriendshipRequestId().uuid().toString(),
                f.getSenderId().uuid().toString(),
                v.senderUsername(),
                v.senderPictureUrl(),
                f.getReceiverId().uuid().toString(),
                f.getStatus().name()
        );
    }
}
