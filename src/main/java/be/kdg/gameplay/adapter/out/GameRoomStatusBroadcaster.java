package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.domain.GameRoom;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GameRoomStatusBroadcaster {

    private final SimpMessagingTemplate messaging;

    public GameRoomStatusBroadcaster(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    // better practice
    public void broadcastStatusUpdate(GameRoom gameRoom) {
        var update = Map.of(
                "gameRoomId", gameRoom.getGameRoomId().uuid().toString(),
                "status", gameRoom.getStatus().name(),
                "invitationStatus", gameRoom.getInvitationStatus().name(),
                "invitedPlayerId", gameRoom.getInvitedPlayerId() != null
                        ? gameRoom.getInvitedPlayerId().uuid().toString()
                        : null
        );

        messaging.convertAndSend(
                "/topic/game-rooms/" + gameRoom.getGameRoomId().uuid(),
                update
        );
    }

    // better practice??
    public void broadcastMatchStarted(String gameRoomId, String matchId) {
        var event = Map.of(
                "type", "MATCH_STARTED",
                "gameRoomId", gameRoomId,
                "matchId", matchId
        );

        messaging.convertAndSend(
                "/topic/game-rooms/" + gameRoomId,
                event
        );
    }

}
