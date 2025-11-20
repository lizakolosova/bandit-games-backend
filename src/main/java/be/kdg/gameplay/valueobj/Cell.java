package be.kdg.gameplay.valueobj;

import java.util.Objects;

public record Cell(int x, int y, String value) {
    public Cell {
        Objects.requireNonNull(value);
    }
}

