package com.gylderia.polis;

import com.gylderia.polis.models.GylderiaPlayer;
import com.gylderia.polis.models.Rank;
import com.gylderia.polis.models.Town;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import com.gylderia.polis.utils.utils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class PlayerManager {

    private MySQLAccess mySQLAccess;
    private Polis plugin;
    private CacheManager cacheManager;

    public PlayerManager(MySQLAccess mySQLAccess, Polis polis) {
        this.mySQLAccess = mySQLAccess;
        this.plugin = polis;
        this.cacheManager = plugin.getCacheManager();
    }

    public void createDefaultPlayer(Player player) {
        System.out.println("Creating default player for " + player.getName());
        UUID uuid = player.getUniqueId();
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);

        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
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

    public void savePlayerTownAndRankToDataBase(UUID uuid, Town town, Rank rank) { //TODO dupliquer la méthode pour gérer le rang par défautt
        //TODO Probablement à supprimer stocker en cache et plus directement en BDD (cf TownCreate)
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        byte[] townUUIDBytes = town.getUuid();
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
                    "UPDATE players SET ville = ?, rang = ? WHERE uuid = ?"
            );
            stmt.setBytes(1, townUUIDBytes);
            stmt.setBytes(2, rank.getUuid());
            stmt.setBytes(3, uuidBytes);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerInDatabase(UUID uuid) {
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
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
        Rank rank;
        byte[] uuidBytes = utils.convertUUIDtoBytes(uniqueId);
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
                    "SELECT * FROM players WHERE uuid = ?"
            );
            stmt.setBytes(1, uuidBytes);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Move cursor to the first row
                byte[] townUUIDBytes = rs.getBytes("ville");
                byte[] rang = rs.getBytes("rang");
                //debug message
                System.out.println("Town UUID: " + Arrays.toString(townUUIDBytes));
                if (cacheManager.getTown(townUUIDBytes) != null) {
                    System.out.println("Player " + playerName + " has a town");
                    Town town = cacheManager.getTown(townUUIDBytes);
                    rank = cacheManager.getTown(townUUIDBytes).getRank(rang);
                    if (rank == null) {
                        System.out.println("Rank not found in town");
                    }
                    System.out.println(cacheManager.getTown(townUUIDBytes));
                    GylderiaPlayer gylderiaPlayer = new GylderiaPlayer(town, uniqueId, playerName, rank);
                    cacheManager.putPlayer(uniqueId, gylderiaPlayer);
                    town.addPlayer(gylderiaPlayer);

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

    public void loadPlayersOnStart() { //TODO doutes sur cette méthode + pas implantée
        try {
            Connection connection = mySQLAccess.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID uuid = utils.convertBytesToUUID(rs.getBytes("uuid"));
                if (plugin.getServer().getPlayer(uuid) != null) {
                    loadPlayer(plugin.getServer().getPlayer(uuid));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}