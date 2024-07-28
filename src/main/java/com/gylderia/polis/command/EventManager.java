package com.gylderia.polis.command;

import com.gylderia.polis.Events.JoinEvent;
import com.gylderia.polis.Polis;
import org.bukkit.Bukkit;

public class EventManager {

    public EventManager(Polis plugin) {
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), plugin);
    }
}
