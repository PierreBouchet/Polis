package com.gylderia.polis.data;

import com.gylderia.polis.ConfigManager;
import com.gylderia.polis.Polis;
import com.gylderia.polis.Towns.PolisPlayer;
import com.gylderia.polis.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {

    private static File polisFolder;
    private static File playerFolder;

    public static HashMap<UUID, Town> TownCache = new HashMap<>();

    public static ArrayList<PolisPlayer> playersList = new ArrayList<>();

    public DataManager(Polis plugin) {
        polisFolder = ConfigManager.polisFolderPath;
        playerFolder = ConfigManager.playerFolder;
        loadTownData();
    }

    public static void loadTownData() {

        for (File townFile : Objects.requireNonNull(polisFolder.listFiles())) {
            YamlConfiguration townData = YamlConfiguration.loadConfiguration(townFile);
            new Town(townData);
        }

    }
    public static boolean isFirstJoin(Player player) {
        File playerFile = new File(playerFolder, player.getUniqueId() + ".yml");
        return !playerFile.exists();
    }
    public static void newPlayer(Player player) {
        File playerFile = new File(playerFolder, player.getUniqueId() + ".yml");
        try {
            playerFile.createNewFile();
            YamlConfiguration modify = YamlConfiguration.loadConfiguration(playerFile);
            modify.set("town", null);
            modify.save(playerFile);
        } catch(IOException e) {
            System.out.println("Le fichier joueur n'a pas pu être créé");
        }
    }

    public static void cachePlayer(Town town) { //sauvegarder en cache les données du joueur
        DataManager.playersList.add(new PolisPlayer(town));
    }

    public static void uncachePlayer() {

    }

    public static void loadPlayerData(UUID uuid) { // récuperer les données du joueur
        Player p = Bukkit.getPlayer(uuid);
        if (isFirstJoin(p)) {
           newPlayer(p);
        }
        File playerFile = new File(playerFolder, uuid.toString() + ".yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(playerFile);
        String town = data.getString("town");
        cachePlayer(); // faire un dictionnaire qui associe String townName, Town town

    }


}
