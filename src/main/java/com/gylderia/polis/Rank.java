package com.gylderia.polis;

import com.gylderia.polis.utils.utils;

import java.util.UUID;

public class Rank {
    private String displayName;
    private boolean isDefault;
    private byte[] uuid;

    private boolean isLeader;

    public Rank(byte[] uuid, String displayName, Boolean isDefault, boolean isLeader) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.isDefault = isDefault;
        this.isLeader = isLeader;
    }

    public Rank(String displayName, Boolean isDefault, boolean isLeader) {
        this.displayName = displayName;
        this.uuid = utils.convertUUIDtoBytes(UUID.randomUUID());
        this.isDefault = isDefault;
        this.isLeader = isLeader;
    }

    public Rank clone() {
        return new Rank(utils.convertUUIDtoBytes(UUID.randomUUID()), this.displayName, this.isDefault, this.isLeader);
    }
    public String getDisplayName() {
        return displayName;
    }

    public boolean  setDisplayName(String displayName) {
        this.displayName = displayName;
        return true;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        return true;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public boolean setLeader(boolean isLeader) {
        this.isLeader = isLeader;
        return true;
    }
}
