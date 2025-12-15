package be.kdg.player.adapter.in.request;

import java.util.UUID;

public record InitiatePurchaseRequest(UUID playerId, UUID gameId) {}