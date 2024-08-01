package com.gylderia.polis.commands;

import com.google.common.cache.Cache;
import com.gylderia.polis.CacheManager;
import com.gylderia.polis.Polis;
import com.gylderia.polis.TownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TownCreate implements CommandExecutor {
    TownManager townManager;
    CacheManager cacheManager;

    public TownCreate(Polis plugin) {

        this.townManager = plugin.getTownManager();
        this.cacheManager = plugin.getCacheManager();
    }
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                if (args.length != 1) {
                    sender.sendMessage("Usage: /towncreate <name>");
                    return true;
                }
                Player player = (Player) sender;
                UUID uuid = player.getUniqueId();
                if (player.hasPermission("polis.town.create")) {
                    if (!cacheManager.getPlayer(player.getUniqueId()).hasTown()) {
                        //create town
                        townManager.newTown(args[0]);
                    } else {
                        player.sendMessage("You are already in a town!");
                    }
                } else {
                    player.sendMessage("You do not have permission to execute the /towncreate command!");
                }
            }
            return true;
        }
}

