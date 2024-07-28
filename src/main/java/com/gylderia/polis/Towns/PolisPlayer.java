package com.gylderia.polis.Towns;

import com.gylderia.polis.data.DataManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.UUID;

public class PolisPlayer {

    private Town town;
    private UUID uuid;

    public PolisPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public PolisPlayer(UUID uuid, Town town) {
        this(uuid);
        this.town = town;
    }



    public boolean hasTown() {
        //si le joueur a une ville, return true
        return false;
    }
    public void getTown() {
        //recup la ville du joueur
    }

    public void getRank() {
        //recup le rang du joueur dans la ville si il a une ville
    }

    //fonction hasTownPermission
}
