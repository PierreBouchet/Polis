package com.gylderia.polis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {

    private Polis plugin;
    public static FileConfiguration config;

    public static File polisFolderPath;

    public static File playerFolder;
    public ConfigManager(Polis plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        plugin.saveDefaultConfig();

        databaseSetup();
    }
    private void databaseSetup() {

        if (!plugin.getDataFolder().exists()) { // créer le dossier de config du plugin s'il n'existe pas
            plugin.getDataFolder().mkdir();
        }

        File playerFolder = new File(plugin.getDataFolder().getAbsolutePath() + "/players");
        File polisFolder = new File(plugin.getDataFolder().getAbsolutePath() + "/polis");
        polisFolderPath = polisFolder;
        ConfigManager.playerFolder = playerFolder;

        playerFolder.mkdir();
        polisFolder.mkdir();

    }


}
