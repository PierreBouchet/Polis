package com.gylderia.polis.commands;

import com.gylderia.polis.*;
import com.gylderia.polis.models.GylderiaPlayer;
import com.gylderia.polis.models.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class joinTown implements CommandExecutor {

    TownManager townManager;
    CacheManager cacheManager;
    PlayerManager playerManager;

    public joinTown(Polis plugin, TownManager townManager) {
        this.townManager = townManager;
        this.cacheManager = plugin.getCacheManager();
        this.playerManager = plugin.getPlayerManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("polis.town.join")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /jointown <town>");
                return true;
            }
            Player player = (Player) sender;
            GylderiaPlayer gylderiaPlayer = cacheManager.getPlayer(player.getUniqueId());
            if (!gylderiaPlayer.hasTown()) {
                //si la ville existe
                if (cacheManager.getTownByName(args[0]) != null) {
                    Town town = cacheManager.getTownByName(args[0]);
                    //si le joueur a une invitation
                    if (town.hasJoinTownInvitation(gylderiaPlayer)) {
                        //on le met dans la ville
                        playerManager.setPlayerTown(player.getUniqueId(), town, town.getDefaultRank());
                        cacheManager.getPlayer(player.getUniqueId()).setTown(town);
                        cacheManager.getPlayer(player.getUniqueId()).setRank(town.getDefaultRank());
                        town.addPlayer(gylderiaPlayer);
                        town.removeJoinTownInvitation(gylderiaPlayer);
                    } else {
                        sender.sendMessage("You do not have an invitation to join this town!");
                    }
                } else {
                    sender.sendMessage("This town does not exist!");
                }
            } else {
                sender.sendMessage("You are already in a town!");
            }
        } else {
            sender.sendMessage("You do not have permission to execute the /jointown command!");
        }
        return true;
    }
}
