package com.gylderia.polis.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class utils {
    public static byte[] convertUUIDtoBytes(UUID uuid) {
        byte[] uuidBytes = new byte[36];
        byte[] uuidStringBytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        System.arraycopy(uuidStringBytes, 0, uuidBytes, 0, uuidStringBytes.length);
        return uuidBytes;

    }
}
