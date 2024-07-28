package com.gylderia.polis.Towns;

import com.gylderia.polis.data.DataManager;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Town {

    public String name;
    public List<UUID> members = new ArrayList<>();
    public List<Chunk> territories = new ArrayList<>();

    public Town(String name, UUID leader, Chunk firstChunk) {

    }

    public Town(YamlConfiguration townData) {
        this.name = townData.getString("name");

        for (String uuidString : townData.getStringList("members")) {
            UUID uuid = UUID.fromString(uuidString);
            this.members.add(uuid);
        }

        DataManager.TownCache.put(UUID.fromString(Objects.requireNonNull(townData.getString("uuid"))), this);
    }

    public String getName() {
        return this.name;
    }

    public List<UUID> getMembers() {
        return this.members;
    }

    public List<Chunk> getTerritories() {
        return this.territories;
    }

}
