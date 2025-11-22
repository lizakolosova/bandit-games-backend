package be.kdg.player.port.in;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.response.PlayerDto;

import java.time.LocalDateTime;

public interface RegisterPlayerUseCase {
    PlayerDto register(RegisterPlayerCommand command);
}
