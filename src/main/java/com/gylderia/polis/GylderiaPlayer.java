package com.gylderia.polis;

import java.util.UUID;

public class GylderiaPlayer {
    private final Town town;
    private final UUID uuid;

    public GylderiaPlayer(Town town, UUID uuid) {
        this.town = town;
        this.uuid = uuid;
    }

    public Town getTown() {
        return town;
    }

    public boolean hasTown() {
        return town != null;
    }
}
