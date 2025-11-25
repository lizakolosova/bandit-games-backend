package be.kdg.player.adapter.in.response;

import be.kdg.player.domain.FriendshipRequest;

public record FriendshipRequestDto(
        String friendshipRequestId,
        String senderId,
        String receiverId,
        String status
) {
    public static FriendshipRequestDto from(FriendshipRequest f) {
        return new FriendshipRequestDto(
                f.getFriendshipRequestId().uuid().toString(),
                f.getSenderId().uuid().toString(),
                f.getReceiverId().uuid().toString(),
                f.getStatus().name()
        );
    }
}