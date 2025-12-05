package be.kdg.gameplay.adapter.in;

import be.kdg.gameplay.adapter.in.request.MatchRequest;
import be.kdg.gameplay.adapter.in.response.GameRoomDto;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.port.in.StartMatchCommand;
import be.kdg.gameplay.port.in.StartMatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameplay")
public class MatchController {

    private final StartMatchUseCase startMatchUseCase;

    public MatchController(StartMatchUseCase startMatchUseCase) {
        this.startMatchUseCase = startMatchUseCase;
    }

    @PostMapping("/matches/start")//change the name later
    public ResponseEntity<GameRoomDto> finalizeRoom(@RequestBody MatchRequest request) {
        StartMatchCommand command = new StartMatchCommand(request.gameRoomId());
        GameRoom gameRoom = startMatchUseCase.start(command);

        return ResponseEntity.ok(GameRoomDto.from(gameRoom));
    }
}