package be.kdg.platform.port.in;

import java.util.Optional;

public record FilterGamesCommand(
        Optional<String> category
) {}

