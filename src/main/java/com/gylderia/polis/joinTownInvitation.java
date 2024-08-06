package com.gylderia.polis;

import com.gylderia.polis.models.GylderiaPlayer;
import com.gylderia.polis.models.Town;

import java.util.Date;

public class joinTownInvitation {
    private final GylderiaPlayer player;
    private final Town town;
    private final Date creationTime;

    public joinTownInvitation(GylderiaPlayer player, Town town) {
        this.player = player;
        this.town = town;
        this.creationTime = new Date();
    }

    public GylderiaPlayer getPlayer() {
        return player;
    }

    public Town getTown() {
        return town;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
