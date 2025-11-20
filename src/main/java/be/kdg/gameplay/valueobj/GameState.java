package be.kdg.gameplay.valueobj;

import be.kdg.common.valueobj.PlayerId;

import java.util.Map;
import java.util.Objects;

public record GameState(
        BoardRepresentation board,
        PlayerId currentTurn,
        Map<String, String> metadata
) {

    public GameState {
        Objects.requireNonNull(board);
        Objects.requireNonNull(metadata);
    }
}


