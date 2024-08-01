package com.gylderia.polis;

import java.util.UUID;

public class GylderiaPlayer {
    private final Town town;
    private final UUID uuid;
    private final String name;

    public GylderiaPlayer(Town town, UUID uuid, String name) {
        this.town = town;
        this.uuid = uuid;
        this.name = name;
    }

    public GylderiaPlayer(UUID uuid, String name) {
        this.name = name;
        this.uuid = uuid;
        this.town = null;
    }

    public Town getTown() {
        if (town == null) {
            throw new IllegalStateException("Player does not have a town");
        }
        return town;
    }

    public boolean hasTown() {
        return town != null;
    }
}
