package com.gylderia.polis;

import com.gylderia.polis.commands.TownCreate;
import com.gylderia.polis.utils.mysql.MySQLAPI;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import com.gylderia.polis.utils.utils;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class TownManager {

    private final Connection connection;
    private final Polis plugin;

    private  final CacheManager cacheManager;
    public TownManager(Connection connection, Polis plugin) {
        this.connection = connection;
        this.plugin = plugin;
        this.cacheManager = plugin.getCacheManager();
        loadAllTowns();
        plugin.getCommand("towncreate").setExecutor(new TownCreate(plugin, this));
    }

    public Town newTown(String name) {
        UUID uuid = UUID.randomUUID();

        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO villes (uuid, name, creation_date) VALUES (?, ?, NOW())"
            );
            stmt.setBytes(1, uuidBytes);
            stmt.setString(2, name);
            stmt.executeUpdate();

            Town town = new Town(name, new Date(), uuidBytes);
            cacheManager.putTown(uuidBytes, town);

            return town;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void loadAllTowns() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM villes");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                Date creationDate = rs.getTimestamp("creation_date");
                byte[] uuidBytes = rs.getBytes("uuid");
                Town town = new Town(name, creationDate, uuidBytes);
                cacheManager.putTown(uuidBytes, town);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
