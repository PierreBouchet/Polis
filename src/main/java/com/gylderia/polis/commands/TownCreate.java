package com.gylderia.polis.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCreate implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // Implementation of the /towncreate command
            //send a message to player if sender is a player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("You have executed the /towncreate command!");
            }
            return true;
        }
}
