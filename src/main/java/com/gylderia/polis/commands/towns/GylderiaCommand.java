package com.gylderia.polis.commands.towns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface GylderiaCommand {
    boolean execute(CommandSender sender, Command command, String label, String[] args);
}