package com.gylderia.polis.commands;

import com.gylderia.polis.CacheManager;
import com.gylderia.polis.PlayerManager;
import com.gylderia.polis.Polis;
import com.gylderia.polis.TownManager;
import com.gylderia.polis.models.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TownInvit implements CommandExecutor {

    TownManager townManager;
    CacheManager cacheManager;
    PlayerManager playerManager;

    public TownInvit(Polis plugin, TownManager townManager) {
        this.townManager = townManager;
        this.cacheManager = plugin.getCacheManager();
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("polis.town.invit")) {
            if (strings.length != 1) {
                commandSender.sendMessage("Usage: /towninvit <player>");
                return true;
            }
            Player player = (Player) commandSender;
            Player target = player.getServer().getPlayer(strings[0]);
            if (target == null) {
                commandSender.sendMessage("This player is not online!");
                return true;
            }
            if (target == player) {
                commandSender.sendMessage("You cannot invite yourself to join your town!");
                return true;
            }
            if (cacheManager.getPlayer(player.getUniqueId()).hasTown()) {
                Town town = cacheManager.getPlayer(player.getUniqueId()).getTown();
                town.addJoinTownInvitation(cacheManager.getPlayer(target.getUniqueId()));
                commandSender.sendMessage("You have invited " + target.getName() + " to join your town!");
                target.sendMessage("You have been invited to join " + cacheManager.getPlayer(player.getUniqueId()).getTown().getName() +  "!");
            } else {
                commandSender.sendMessage("You are not in a town!");
            }
        } else {
            commandSender.sendMessage("You do not have permission to execute the /towninvit command!");
        }

        return false;
    }
}
