package com.gylderia.polis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    // Load all configuration files from the plugin's data folder
    private void loadConfigs() {
        File configDir = plugin.getDataFolder();
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        copyMissingResources(configDir);
        loadConfigFiles(configDir);
    }

    // Copy missing resources from the JAR to the plugin's data folder
    private void copyMissingResources(File configDir) {
        try {
            URL resourceUrl = plugin.getClass().getClassLoader().getResource("");
            if (resourceUrl == null) {
                plugin.getLogger().log(Level.WARNING, "Resource directory not found.");
                return;
            }
            Path sourceDir = new File(resourceUrl.toURI()).toPath();
            Files.walk(sourceDir).forEach(source -> {
                Path destination = configDir.toPath().resolve(sourceDir.relativize(source));
                try {
                    if (Files.isDirectory(source)) {
                        if (!Files.exists(destination)) {
                            Files.createDirectories(destination);
                        }
                    } else {
                        if (!Files.exists(destination)) {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load all .yml configuration files from the specified directory
    private void loadConfigFiles(File configDir) {
        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                configs.put(file.getName(), YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    // Get a specific configuration file by name
    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    // Save a specific configuration file by name
    public void saveConfig(String fileName) {
        FileConfiguration config = configs.get(fileName);
        if (config != null) {
            try {
                config.save(new File(plugin.getDataFolder(), fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}