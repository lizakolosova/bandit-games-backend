package be.kdg.player.adapter.out;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
public class FriendEmbeddable {

    private UUID friendId;
    private LocalDateTime since;

    protected FriendEmbeddable() {}

    public FriendEmbeddable(UUID friendId, LocalDateTime since) {
        this.friendId = friendId;
        this.since = since;
    }

    public UUID getFriendId() { return friendId; }
    public LocalDateTime getSince() { return since; }
}

