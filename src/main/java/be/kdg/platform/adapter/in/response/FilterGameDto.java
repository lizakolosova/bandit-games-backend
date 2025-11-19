package be.kdg.platform.adapter.in.response;

public record FilterGameDto(
        String id,
        String name,
        String pictureUrl,
        String category,
        int averageMinutes
) {}
