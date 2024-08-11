package com.gylderia.polis.utils;

import org.bukkit.Chunk;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class utils {
    public static byte[] convertUUIDtoBytes(UUID uuid) {
        byte[] uuidBytes = new byte[36];
        byte[] uuidStringBytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        System.arraycopy(uuidStringBytes, 0, uuidBytes, 0, uuidStringBytes.length);
        return uuidBytes;

    }

    public static UUID convertBytesToUUID(byte[] uuids) {
        String uuidString = new String(uuids, StandardCharsets.UTF_8);
        return UUID.fromString(uuidString);
    }

    public static int[] getCoordinatesFromKey(long chunkKey) {
        int x = (int) (chunkKey & 4294967295L);
        int z = (int) (chunkKey >> 32);
        return new int[]{x, z};
    }

}
