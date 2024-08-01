package com.gylderia.polis.commands;

import com.gylderia.polis.CacheManager;
import com.gylderia.polis.Polis;
import com.gylderia.polis.Town;
import com.gylderia.polis.GylderiaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class DebugCacheCommand implements CommandExecutor {

    private final CacheManager cacheManager;

    public DebugCacheCommand(Polis plugin) {
        this.cacheManager = plugin.getCacheManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Cache Debug Information:");

        // Display player cache
        sender.sendMessage("Player Cache:");
        for (Map.Entry<UUID, GylderiaPlayer> entry : cacheManager.getPlayerCache().entrySet()) {
            sender.sendMessage("UUID: " + entry.getKey() + ", Town: " + entry.getValue().getTown() + ", Name: " + entry.getValue().getName());
        }

        // Display town cache
        sender.sendMessage("Town Cache:");
        for (Map.Entry<byte[], Town> entry : cacheManager.getTownCache().entrySet()) {
            sender.sendMessage("UUID: " + Arrays.toString(entry.getValue().getUuid()) + ", Name: " + entry.getValue().getName());
        }

        return true;
    }
}