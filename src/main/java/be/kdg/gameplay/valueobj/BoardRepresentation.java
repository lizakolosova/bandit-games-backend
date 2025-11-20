package be.kdg.gameplay.valueobj;

import java.util.List;
import java.util.Objects;

public record BoardRepresentation(List<Cell> cells) {

    public BoardRepresentation {
        Objects.requireNonNull(cells);
    }
}

