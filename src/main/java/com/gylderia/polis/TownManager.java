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
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
                    "INSERT INTO rangs (rang, displayName, isDefault, isLeader, ville) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setBytes(1, rank.getUuid());
            stmt.setString(2, new String(rank.getDisplayName().getBytes(), StandardCharsets.UTF_8));
            stmt.setBoolean(3, rank.isDefault());
            stmt.setBoolean(4, rank.isLeader());
            stmt.setBytes(5, town.getUuid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Town newTown(String name) {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = utils.convertUUIDtoBytes(uuid);
        Map<byte[], Rank> newTownRankList = cloneDefaultRankList();
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement(
                    "INSERT INTO villes (uuid, name, creation_date) VALUES (?, ?, NOW())"
            );
            stmt.setBytes(1, uuidBytes);
            stmt.setString(2, name);
            stmt.executeUpdate();

            Town town = new Town(name, new Date(), uuidBytes, newTownRankList);  //TODO ajouter les rangs par défaut dans map<String , Rank>
            cacheManager.putTown(uuidBytes, town);
            //pour chaque rang dans newTownRankList, appeler la méthode addRankToDataBase
            for (Rank rank : newTownRankList.values()) {
                addRankToDataBase(rank, town);
            }
            return town;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void loadAllTowns() {
        try {
            PreparedStatement stmt = mySQLAccess.getConnection().prepareStatement("SELECT * FROM villes");
            ResultSet rs = stmt.executeQuery();
            Map<byte[], Rank> rankList = new HashMap<>();
            while (rs.next()) {
                try {
                    PreparedStatement rankStmt = mySQLAccess.getConnection().prepareStatement("SELECT * FROM rangs WHERE ville = ?");
                    rankStmt.setBytes(1, rs.getBytes("uuid"));
                    ResultSet rsRanks = rankStmt.executeQuery();
                    while (rsRanks.next()) {
                        byte[] rankUUID = rsRanks.getBytes("rang");
                        String displayName = rsRanks.getString("displayName");
                        boolean isDefault = rsRanks.getBoolean("isDefault");
                        boolean isLeader = rsRanks.getBoolean("isLeader");
                        Rank rank = new Rank(rankUUID, displayName, isDefault, isLeader);
                        rankList.put(rankUUID, rank);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String name = rs.getString("name");
                Date creationDate = rs.getTimestamp("creation_date");
                byte[] townUUID = rs.getBytes("uuid");
                //TODO mettre en cache les rangs de la ville (défault + custom)
                Town town = new Town(name, creationDate, townUUID,  rankList);
                cacheManager.putTown(townUUID, town);
            }
        } catch (Exception e) {
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
