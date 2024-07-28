package com.gylderia.polis;

import com.gylderia.polis.command.CommandManager;
import com.gylderia.polis.command.EventManager;
import com.gylderia.polis.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Polis extends JavaPlugin {

    @Override
    public void onEnable() {
        new ConfigManager(this);
        new CommandManager(this);
        new DataManager(this);
        new EventManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
