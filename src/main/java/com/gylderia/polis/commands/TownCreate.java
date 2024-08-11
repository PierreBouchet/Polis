package com.gylderia.polis.commands;

import com.gylderia.polis.*;
import com.gylderia.polis.models.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TownCreate extends BaseCommand {

    public TownCreate(Polis plugin) {

        super(plugin);

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
                        Town town = townManager.newTown(args[0]);
                        //save in bdd
                        playerManager.savePlayerTownAndRankToDataBase(uuid, town, town.getLeaderRank()); //TODO stocker en cache et plus directement en BDD (voir PlayerManager)
                        cacheManager.getPlayer(uuid).setTown(town);
                        cacheManager.getPlayer(uuid).setRank(town.getLeaderRank());
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

