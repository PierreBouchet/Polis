package com.gylderia.polis;

import com.gylderia.polis.commands.chunkInfo;
import com.gylderia.polis.commands.claimChunk;
import com.gylderia.polis.commands.unclaimChunk;
import com.gylderia.polis.models.GylderiaChunk;
import com.gylderia.polis.models.Town;
import com.gylderia.polis.utils.mysql.MySQLAccess;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ChunkManager {

    private final MySQLAccess mySQLAccess;
    private final Polis polis;
    private final CacheManager cacheManager;
    private final TownManager townManager;

    public ChunkManager(MySQLAccess mySQLAccess, Polis polis) {
        this.mySQLAccess = mySQLAccess;
        this.polis = polis;
        this.cacheManager = polis.getCacheManager();
        this.townManager = polis.getTownManager();
        cacheChunks();
    }

    public void registerCommands() {
        polis.getCommand("chunkinfo").setExecutor(new chunkInfo(polis));
        polis.getCommand("claimchunk").setExecutor(new claimChunk(polis));
        polis.getCommand("unclaimchunk").setExecutor(new unclaimChunk(polis));
    }

    public void cacheChunks() {
        try {
            Connection connection = mySQLAccess.getConnection();
            String query = "SELECT * FROM chunks";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long chunkKey = resultSet.getLong("chunkKey");
                Town town = cacheManager.getTown(resultSet.getBytes("townUUID"));
                World world = polis.getServer().getWorld(resultSet.getString("world"));
                Chunk chunk = world.getChunkAt(chunkKey);
                GylderiaChunk gylderiaChunk = new GylderiaChunk(chunk, town);
                cacheManager.putChunk(gylderiaChunk);
                town.addChunk(gylderiaChunk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isChunkClaimed(Chunk chunk) {
        long key = chunk.getChunkKey();
        return cacheManager.getChunk(key) != null && cacheManager.getChunk(key).getTown() != null;
    }


    public void claimChunk(Chunk chunk, Town town) {
        long key = chunk.getChunkKey();
        if (cacheManager.isChunkCached(key)) {
            cacheManager.getChunk(key).setTown(town);
        } else {
            cacheManager.putChunk(new GylderiaChunk(chunk, town));
        }
        town.addChunk(cacheManager.getChunk(key));
        try (Connection connection = mySQLAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO chunks (chunkKey, townUUID, world) VALUES (?, ?, ?)"
            )) {
            preparedStatement.setLong(1, key);
            preparedStatement.setBytes(2, town.getUuid());
            preparedStatement.setString(3, chunk.getWorld().getName());
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unclaimChunk(Chunk chunk) {
        long key = chunk.getChunkKey();
        if (cacheManager.isChunkCached(key)) {
            cacheManager.getChunk(key).setTown(null);
        }
        try (Connection connection = mySQLAccess.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM chunks WHERE chunkKey = ?"
            )) {
            preparedStatement.setLong(1, key);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}