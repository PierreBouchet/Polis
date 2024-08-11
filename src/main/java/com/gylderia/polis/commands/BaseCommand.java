package com.gylderia.polis.commands;

import com.gylderia.polis.*;
import org.bukkit.command.CommandExecutor;

public abstract class BaseCommand implements CommandExecutor {
    protected TownManager townManager;
    protected CacheManager cacheManager;
    protected PlayerManager playerManager;

    protected ChunkManager chunkManager;

    public BaseCommand(Polis plugin) {
        this.townManager = plugin.getTownManager();
        this.cacheManager = plugin.getCacheManager();
        this.playerManager = plugin.getPlayerManager();
        this.chunkManager = plugin.getChunkManager();
    }
}