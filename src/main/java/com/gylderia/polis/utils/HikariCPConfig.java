package com.gylderia.polis.utils;

import com.gylderia.polis.Polis;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class HikariCPConfig {
    private HikariDataSource dataSource;

    public HikariCPConfig(Polis plugin) {
        File configFile = new File(plugin.getDataFolder(), "mysql/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getInt("mysql.port") + "/" + config.getString("mysql.database"));
        hikariConfig.setUsername(config.getString("mysql.username"));
        hikariConfig.setPassword(config.getString("mysql.password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}