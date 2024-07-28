package com.gylderia.polis;

import com.gylderia.polis.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Polis extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize the ConfigManager and load configurations
        configManager = new ConfigManager(this);
        configManager.setupConfig();
        getLogger().info("Polis plugin enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Polis plugin disabled");
    }

}