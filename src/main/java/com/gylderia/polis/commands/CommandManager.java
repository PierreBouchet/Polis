package com.gylderia.polis.commands;

import com.gylderia.polis.commands.towns.GylderiaCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {
    private final Map<String, GylderiaCommand> commands = new HashMap<>();

    public void registerCommand(String name, GylderiaCommand command) {
        commands.put(name, command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GylderiaCommand gylderiaCommand = commands.get(label);
        if (gylderiaCommand != null) {
            return gylderiaCommand.execute(sender, command, label, args);
        }
        return false;
    }

    public void registerCommands(JavaPlugin plugin) {
        for (String command : commands.keySet()) {
            plugin.getCommand(command).setExecutor(this);
        }
    }
}