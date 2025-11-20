package be.kdg.platform.adapter.in.response;

import java.time.LocalDate;

public record AddGameRequest(
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int averageMinutes
) {}
