package com.gylderia.polis;

import com.gylderia.polis.utils.utils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class PlayerManager {

    private Connection connection;
    private Polis plugin;
    private CacheManager cacheManager;

    public PlayerManager(Connection connection, Polis polis) {
        this.connection = connection;
        this.plugin = polis;
        this.cacheManager = plugin.getCacheManager();
    }

    public void createDefaultPlayer(Player player) {
        System.out.println("Creating default player for " + player.getName());
        UUID uuid = player.getUniqueId();
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO players (uuid, name) VALUES (?, ?)"
            );
            stmt.setBytes(1, uuidBytes);
            stmt.setString(2, player.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cacheManager.putPlayer(uuid, new GylderiaPlayer(uuid, player.getName()));
        System.out.println("Player cached: " + (cacheManager.getPlayer(uuid) != null));
    }

    public void setPlayerTown(UUID uuid, Town town) {
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        byte[] townUUIDBytes = town.getUuid();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE players SET ville = ? WHERE uuid = ?"
            );
            stmt.setBytes(1, townUUIDBytes);
            stmt.setBytes(2, uuidBytes);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerInDatabase(UUID uuid) {
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM players WHERE uuid = ?"
            );
            stmt.setBytes(1, uuidBytes);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadPlayer(Player player) {
        System.out.println("Loading player " + player.getName());
        UUID uniqueId = player.getUniqueId();
        String playerName = player.getName();
        byte[] uuidBytes = utils.convertUUIDtoBytes(uniqueId);
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM players WHERE uuid = ?"
            );
            stmt.setBytes(1, uuidBytes);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Move cursor to the first row
                byte[] townUUIDBytes = rs.getBytes("ville");
                //debug message
                System.out.println("Town UUID: " + Arrays.toString(townUUIDBytes));
                if (townUUIDBytes != null) {
                    System.out.println(cacheManager.getTown(townUUIDBytes));
                    System.out.println("Player " + playerName + " has a town");
                    cacheManager.putPlayer(uniqueId, new GylderiaPlayer(cacheManager.getTown(townUUIDBytes), uniqueId, playerName));
                } else {
                    System.out.println("Player " + playerName + " does not have a town");
                    cacheManager.putPlayer(uniqueId, new GylderiaPlayer(uniqueId, playerName));
                }
            } else {
                System.out.println("No player found with UUID: " + uniqueId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}