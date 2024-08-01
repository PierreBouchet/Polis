package com.gylderia.polis;

import java.util.Date;
import java.util.UUID;

public class Town {
    private final String name;
    private final Date creationDate;

    private final byte[] uuid;

    public Town(String name, Date creationDate, byte[] uuid) {
        this.name = name;
        this.creationDate = creationDate;
        this.uuid = uuid;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }


    public Date getCreationDate() {
        return creationDate;
    }

}
