package com.gylderia.polis.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void setupConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        setupConfigFile("mysql/config.yml");
        setupConfigFile("ranks.yml");
    }
    public void setupConfigFile(String filePath) {

        // Create a File object for the configuration file
        File configFile = new File(plugin.getDataFolder(), filePath);

        // If the configuration file does not exist, create it from the resource in the jar file
        if (!configFile.exists()) {
            plugin.saveResource(filePath, false);
        }

        // Load the configuration from the file into the config object
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(filePath, config);
        configFiles.put(filePath, configFile);
    }

    public FileConfiguration getConfig(String filePath) {
        return configs.get(filePath);
    }

    public void saveConfig(String filePath) {
        try {
            configs.get(filePath).save(configFiles.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(String filePath) {
        File configFile = configFiles.get(filePath);
        configs.put(filePath, YamlConfiguration.loadConfiguration(configFile));
    }
}