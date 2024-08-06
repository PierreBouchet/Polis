package com.gylderia.polis;

import com.gylderia.polis.models.GylderiaPlayer;
import com.gylderia.polis.models.Town;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheManager {

    private final Map<UUID, GylderiaPlayer> playerCache = new HashMap<>();
    private final Map<byte[], Town> townCache = new HashMap<>();

    public void putTown(byte[] uuid, Town town) {
        townCache.put(uuid, town);
    }

    public void putPlayer(UUID uuid, GylderiaPlayer gylderiaPlayer) {
        playerCache.put(uuid, gylderiaPlayer);
    }

    public Town getTown(byte[] uuidBytes) {
        for (byte[] key : townCache.keySet()) {
            if (Arrays.equals(key, uuidBytes)) {
                return townCache.get(key);
            }
        }
        return null;
    }

    public GylderiaPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }

    public void removeTown(byte[] uuid) {
        townCache.remove(uuid);
    }

    public void removePlayer(UUID uuid) {
        playerCache.remove(uuid);
    }

    public Map<UUID, GylderiaPlayer> getPlayerCache() {
        return playerCache;
    }

    public Map<byte[], Town> getTownCache() {
        return townCache;
    }

    public Town getTownByName(String name) {
        for (Town town : townCache.values()) {
            if (town.getName().equals(name)) {
                return town;
            }
        }
        return null;
    }


}


