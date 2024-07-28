package com.gylderia.polis;

import com.gylderia.polis.utils.DatabaseUtil;
import com.gylderia.polis.utils.HikariCPConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class Polis extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        HikariCPConfig hikariCPConfig = new HikariCPConfig(this);
        DatabaseUtil.connect(hikariCPConfig);
        getLogger().info("Connected to MySQL database using HikariCP");
        getLogger().info("Polis plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseUtil.disconnect();
        getLogger().info("Disconnected from MySQL database using HikariCP");
        getLogger().info("Polis plugin disabled");
    }
}