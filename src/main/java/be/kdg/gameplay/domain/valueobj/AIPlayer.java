package be.kdg.gameplay.domain.valueobj;

import be.kdg.common.valueobj.PlayerId;

import java.util.UUID;

public final class AIPlayer {
    public static final PlayerId AI_PLAYER =
            PlayerId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
}

