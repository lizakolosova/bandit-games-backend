package be.kdg.player.core;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.player.adapter.in.response.PlayerDto;
import be.kdg.player.domain.Player;
import be.kdg.player.port.in.RegisterPlayerCommand;
import be.kdg.player.port.in.RegisterPlayerUseCase;
import be.kdg.player.port.out.LoadPlayerPort;
import be.kdg.player.port.out.UpdatePlayerPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterPlayerUseCaseImpl implements RegisterPlayerUseCase {

    private final UpdatePlayerPort updatePlayerPort;

    public RegisterPlayerUseCaseImpl(UpdatePlayerPort updatePlayerPort) {
        this.updatePlayerPort = updatePlayerPort;
    }

    @Override
    @Transactional
    public Player register(RegisterPlayerCommand command) {
        Player player = new Player(command.username(), command.email(), null);

        updatePlayerPort.update(player);

        return new Player(player.getPlayerId(), player.getUsername(), player.getEmail(),
                player.getPictureUrl(), player.getCreatedAt());
    }
}
