package com.gylderia.polis.commands;

import com.gylderia.polis.CacheManager;
import com.gylderia.polis.Polis;
import com.gylderia.polis.Town;
import com.gylderia.polis.GylderiaPlayer;
import com.gylderia.polis.Rank;
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
            GylderiaPlayer player = entry.getValue();
            sender.sendMessage("UUID: " + entry.getKey() + ", Name: " + player.getName());
            if (player.hasTown()) {
                Town town = player.getTown();
                sender.sendMessage("Town: " + town.getName());
                //send player rank
                if (player.getRank() == null) { //TODO delete after debug
                    sender.sendMessage("Rank: None");
                } else {
                    sender.sendMessage("Rank: " + player.getRank().getDisplayName());
                }
            } else {
                sender.sendMessage("Town: None");
            }
        }

        // Display town cache
        sender.sendMessage("Town Cache:");
        for (Map.Entry<byte[], Town> entry : cacheManager.getTownCache().entrySet()) {
            Town town = entry.getValue();
            sender.sendMessage("UUID: " + Arrays.toString(town.getUuid()) + ", Name: " + town.getName());
            sender.sendMessage("Ranks in Town:");
            for (Rank rank : town.getRanks().values()) {
                sender.sendMessage("Display Name: " + rank.getDisplayName() + ", Default: " + rank.isDefault() + ", Leader: " + rank.isLeader());
            }
        }

        return true;
    }
}