package com.gylderia.polis;

import com.gylderia.polis.commands.TownCreate;
import com.gylderia.polis.commands.TownInvit;
import com.gylderia.polis.commands.joinTown;
import com.gylderia.polis.models.Rank;
import com.gylderia.polis.models.Town;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import com.gylderia.polis.utils.utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class TownManager {

    private final MySQLAccess mySQLAccess;
    private final Polis plugin;

    private Map<byte[], Rank> defaultRankList = new HashMap<>();
    private FileConfiguration rankConfig;

    private  final CacheManager cacheManager;
    public TownManager(MySQLAccess mySQLAccess, Polis plugin) {
        this.mySQLAccess = mySQLAccess;
        this.plugin = plugin;
        this.cacheManager = plugin.getCacheManager();
        getRankConfig();
        loadAllTowns();
    }

    public void registerCommands() {
        plugin.getCommand("towncreate").setExecutor(new TownCreate(plugin));
        plugin.getCommand("jointown").setExecutor(new joinTown(plugin));
        plugin.getCommand("towninvite").setExecutor(new TownInvit(plugin));
    }

    public boolean getRankConfig() {
        boolean isThereDefaultRank = false;
        boolean isThereLeaderRank = false;
        this.rankConfig = plugin.getConfigManager().getConfig("ranks.yml");
        for (String key : rankConfig.getConfigurationSection("ranks").getKeys(false)) {
            String displayName = rankConfig.getString("ranks." + key + ".displayName");
            boolean isDefault = rankConfig.getBoolean("ranks." + key + ".default", false);
            boolean isLeader = rankConfig.getBoolean("ranks." + key + ".leader", false);
            if (isDefault) {
                isThereDefaultRank = true;
            }
            if (isLeader) {
                isThereLeaderRank = true;
            }
            Rank rank = new Rank(displayName, isDefault, isLeader);
            defaultRankList.put(rank.getUuid(), rank);
        }
        if (!isThereDefaultRank) {
            plugin.getLogger().log(Level.SEVERE, "No default rank found in ranks.yml");
            //TODO ajouter un rang par défaut via un enum par exemple
            return false;
        }
        if (!isThereLeaderRank) {
            plugin.getLogger().log(Level.SEVERE, "No leader rank found in ranks.yml");
            //TODO ajouter un rang leader par défaut via un enum par exemple
            return false;
        }
        //debug message
        for (Rank rank : defaultRankList.values()) {
            plugin.getLogger().info("Rank: " + rank.getDisplayName() + ", Default: " + rank.isDefault() + ", Leader: " + rank.isLeader());
        }
        return true;
    }

    public Map<byte[], Rank> cloneDefaultRankList() {
        Map<byte[], Rank> townDefaultRankList = new HashMap<>();
        for (Rank rank : defaultRankList.values()) {
            Rank clonedRank = rank.clone();
            townDefaultRankList.put(rank.clone().getUuid(), clonedRank);
        }
        return townDefaultRankList;
    }

    public void addRankToDataBase(Rank rank, Town town) {
        try (Connection conn = mySQLAccess.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO rangs (rang, displayName, isDefault, isLeader, ville) VALUES (?, ?, ?, ?, ?)"
            )) {
            stmt.setBytes(1, rank.getUuid());
            stmt.setString(2, new String(rank.getDisplayName().getBytes(), StandardCharsets.UTF_8));
            stmt.setBoolean(3, rank.isDefault());
            stmt.setBoolean(4, rank.isLeader());
            stmt.setBytes(5, town.getUuid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to add rank to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Town newTown(String name) {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        Map<byte[], Rank> newTownRankList = cloneDefaultRankList();
        plugin.getLogger().info("Creating town '" + name + "' with UUID: " + uuid);
        plugin.getLogger().info("UUID bytes length: " + uuidBytes.length);
        try (Connection conn = mySQLAccess.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO villes (uuid, name, creation_date) VALUES (?, ?, NOW())"
            )) {
            stmt.setBytes(1, uuidBytes);
            stmt.setString(2, name);
            plugin.getLogger().info("Executing INSERT into villes table...");
            stmt.executeUpdate();
            plugin.getLogger().info("Town inserted into database successfully");

            Town town = new Town(name, new Date(), uuidBytes, newTownRankList);  //TODO ajouter les rangs par défaut dans map<String , Rank>
            cacheManager.putTown(uuidBytes, town);
            //pour chaque rang dans newTownRankList, appeler la méthode addRankToDataBase
            plugin.getLogger().info("Adding " + newTownRankList.size() + " ranks to database...");
            for (Rank rank : newTownRankList.values()) {
                addRankToDataBase(rank, town);
            }
            plugin.getLogger().info("Town '" + name + "' created successfully");
            return town;
        } catch (SQLException e) {
            plugin.getLogger().severe("FAILED to create town '" + name + "': " + e.getMessage());
            plugin.getLogger().severe("SQL State: " + e.getSQLState());
            plugin.getLogger().severe("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return null;
        }
    }


    public void loadAllTowns() {
        try (Connection conn = mySQLAccess.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM villes");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<byte[], Rank> rankList = new HashMap<>();
                byte[] townUUID = rs.getBytes("uuid");

                try (Connection connRank = mySQLAccess.getConnection();
                     PreparedStatement rankStmt = connRank.prepareStatement("SELECT * FROM rangs WHERE ville = ?")) {
                    rankStmt.setBytes(1, townUUID);
                    try (ResultSet rsRanks = rankStmt.executeQuery()) {
                        while (rsRanks.next()) {
                            byte[] rankUUID = rsRanks.getBytes("rang");
                            String displayName = rsRanks.getString("displayName");
                            boolean isDefault = rsRanks.getBoolean("isDefault");
                            boolean isLeader = rsRanks.getBoolean("isLeader");
                            Rank rank = new Rank(rankUUID, displayName, isDefault, isLeader);
                            rankList.put(rankUUID, rank);
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to load ranks for town: " + e.getMessage());
                    e.printStackTrace();
                }

                String name = rs.getString("name");
                Date creationDate = rs.getTimestamp("creation_date");
                //TODO mettre en cache les rangs de la ville (défault + custom)
                Town town = new Town(name, creationDate, townUUID, rankList);
                cacheManager.putTown(townUUID, town);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load towns: " + e.getMessage());
            e.printStackTrace();
        }
        //debug message
        for (Town town : cacheManager.getTownCache().values()) {
            plugin.getLogger().info("Town: " + town.getName());
            for (Rank rank : town.getRanks().values()) {
                plugin.getLogger().info("Rank: " + rank.getDisplayName() + ", Default: " + rank.isDefault() + ", Leader: " + rank.isLeader());
            }
        }
    }



}
