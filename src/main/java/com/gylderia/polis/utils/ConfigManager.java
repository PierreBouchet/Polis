package com.gylderia.polis.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    public  ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        // Create a File object for the configuration directory
        File configDir = new File(plugin.getDataFolder(), "mysql");

        // If the configuration directory does not exist, create it
        if (!configDir.exists()) {
            configDir.mkdir();
        }

        // Create a File object for the configuration file
        configFile = new File(configDir, "config.yml");

        // If the configuration file does not exist, create it from the resource in the jar file
        if (!configFile.exists()) {
            plugin.saveResource("mysql/config.yml", false);
        }

        // Load the configuration from the file into the config object
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Returns the FileConfiguration instance which represents the configuration in the file.
     * @return The FileConfiguration instance.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Saves the current configuration to the file.
     * If an error occurs during saving, it prints the stack trace of the exception.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the configuration from the file.
     * If the file has been modified, the changes will be reflected in the FileConfiguration instance.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
