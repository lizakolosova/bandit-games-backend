package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.adapter.out.dto.GameRoomStatusUpdateDto;
import be.kdg.gameplay.adapter.out.dto.MatchStartedEventDto;
import be.kdg.gameplay.domain.GameRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameRoomStatusBroadcaster {

    private final SimpMessagingTemplate messaging;
    private final Logger log = LoggerFactory.getLogger(GameRoomStatusBroadcaster.class);

    public GameRoomStatusBroadcaster(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void broadcastStatusUpdate(GameRoom gameRoom) {
        String destination = "/topic/game-rooms/" + gameRoom.getGameRoomId().uuid();

        var payload = new GameRoomStatusUpdateDto(
                gameRoom.getGameRoomId().uuid(),
                gameRoom.getStatus().name(),
                gameRoom.getInvitationStatus().name(),
                gameRoom.getInvitedPlayerId() != null ? gameRoom.getInvitedPlayerId().uuid() : null
        );

        log.debug("Broadcasting status update to {}: {}", destination, payload);
        messaging.convertAndSend(destination, payload);
    }

    public void broadcastMatchStarted(UUID gameRoomId, UUID matchId) {
        String destination = "/topic/game-rooms/" + gameRoomId;
        var event = new MatchStartedEventDto(gameRoomId, matchId);

        log.info("Match started! Broadcasting to {}", destination);
        messaging.convertAndSend(destination, event);
    }
}
