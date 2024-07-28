package com.gylderia.polis;

import com.gylderia.polis.utils.ConfigManager;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public final class Polis extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize the ConfigManager and load configurations
        ConfigManager configManager = new ConfigManager(this);
        configManager.setupConfig(); // Ensure the config is set up before accessing it

        String host = configManager.getConfig().getString("host");
        String port = configManager.getConfig().getString("port");
        String database = configManager.getConfig().getString("database");
        String username = configManager.getConfig().getString("username");
        String password = configManager.getConfig().getString("password");

        MySQLAccess mySQLAccess = new MySQLAccess();
        try {
            mySQLAccess.establishConnection(host, port, database, username, password);
            getLogger().info("Successfully established a connection to the database.");
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE,"Could not establish a connection to the database: ", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Polis plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Polis plugin disabled");
    }
}