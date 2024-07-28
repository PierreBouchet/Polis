package com.gylderia.polis.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TownCreate implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (p.hasPermission("polis.create")) { // si le joueur a la permission
                // si le joueur n'est pas membre d'une faction
                //create faction
                String townName;
                UUID leaderUUID = p.getUniqueId();

            } else {
                //ErrorManager (ENUM)
            }


        }

        return false;
    }
}
