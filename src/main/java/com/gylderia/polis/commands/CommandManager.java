package com.gylderia.polis.commands;

import com.gylderia.polis.Polis;
import org.bukkit.plugin.Plugin;

public class CommandManager {

    public CommandManager(Polis plugin) {
        //register TownCreate
        plugin.getCommand("towncreate").setExecutor(new TownCreate());
    }

}
