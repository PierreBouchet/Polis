package com.gylderia.polis.commands;

import com.gylderia.polis.Polis;
import com.gylderia.polis.commands.towns.TownCreate;

public class CommandRegistration {
    private final CommandManager commandManager;

    public CommandRegistration(Polis plugin) {
        commandManager = new CommandManager();
        registerCommands();
        commandManager.registerCommands(plugin);
    }

    private void registerCommands() {
        //commandManager.registerCommand("example", new ExampleCommand());
        commandManager.registerCommand("townCreate", new TownCreate());
    }
}