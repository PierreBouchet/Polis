package com.gylderia.polis;

import com.gylderia.polis.commands.DebugCacheCommand;
import com.gylderia.polis.listeners.PlayerJoinListener;
import com.gylderia.polis.utils.ConfigManager;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

public final class Polis extends JavaPlugin {

    private TownManager townManager;

    private CacheManager cacheManager;
    private PlayerManager playerManager;

    private ConfigManager configManager;

    private ChunkManager chunkManager;

    @Override
    public void onEnable() {
        // Initialize the ConfigManager and load configurations
         this.configManager = new ConfigManager(this);
        configManager.setupConfig(); // Ensure the config is set up before accessing it

        FileConfiguration mysqlConfigFile = configManager.getConfig("mysql/config.yml");

        MySQLAccess mySQLAccess = new MySQLAccess(mysqlConfigFile);
            if (!mySQLAccess.connect()) {
                getLogger().log(Level.SEVERE,"Could not establish a connection to the database: ");
                getServer().getPluginManager().disablePlugin(this);
            }
        this.cacheManager = new CacheManager();
        this.townManager = new TownManager(mySQLAccess, this);
        this.chunkManager = new ChunkManager(mySQLAccess, this);
        this.playerManager = new PlayerManager(mySQLAccess, this);

        registerEvents();
        registerCommands();

        getLogger().info("Polis plugin enabled");
    }

    public void registerCommands() {
        this.getCommand("debugcache").setExecutor(new DebugCacheCommand(this));
        this.townManager.registerCommands();
        this.chunkManager.registerCommands();
    }


    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Polis plugin disabled");
    }

    public TownManager getTownManager() {
        return townManager;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }
}