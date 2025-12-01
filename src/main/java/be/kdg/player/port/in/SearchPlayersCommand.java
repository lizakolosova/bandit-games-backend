package be.kdg.player.port.in;

import java.util.UUID;

public record SearchPlayersCommand(UUID loggedInPlayerId, String query) { }

