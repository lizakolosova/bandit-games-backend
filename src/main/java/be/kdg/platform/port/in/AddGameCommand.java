package be.kdg.platform.port.in;

import java.time.LocalDate;

public record AddGameCommand(
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int averageMinutes
) {}

