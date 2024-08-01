package com.gylderia.polis;

import org.bukkit.entity.Player;

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
            //print uuidBytes with debug info
            System.out.println("db: " + Arrays.toString(uuidBytes));
            //print key with debug info
            System.out.println("cache: " + Arrays.toString(key));
            if (Arrays.equals(key, uuidBytes)) {
                //debug message
                System.out.println("Town found in cache");
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


}


