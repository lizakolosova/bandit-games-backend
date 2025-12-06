package be.kdg.player.port.in.command;

import java.util.UUID;

public record SearchPlayersCommand(UUID loggedInPlayerId, String query) { }

