package com.gylderia.polis.commands.towns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCreate implements GylderiaCommand {
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("You have executed the townCreate command!");
            return true;
        }

        return false;
    }
}
