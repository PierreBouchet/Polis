package com.gylderia.polis.command;

import com.gylderia.polis.Polis;

public class CommandManager {
    private Polis plugin;
    public CommandManager(Polis plugin) {
        this.plugin = plugin;
       commandRegister();
    }

    private void commandRegister() { //déclarer les commandes
        plugin.getCommand("towncreate").setExecutor(new TownCreate());
        plugin.getCommand("townlist").setExecutor(new TownList());
    }



}
