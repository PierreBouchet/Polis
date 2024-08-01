package com.gylderia.polis;

import java.util.UUID;

public class GylderiaPlayer {
    private  Town town;
    private final UUID uuid;
    private  String name;

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

    public void setTown(Town town) {
        if (this.town != null) {
            throw new IllegalStateException("Player already has a town");
        }
        this.town = town;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasTown() {
        return town != null;
    }
}
