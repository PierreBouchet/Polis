package com.gylderia.polis.Events;

import com.gylderia.polis.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class leaveEvent implements Listener {
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        DataManager.loadPlayerData(e.getPlayer().getUniqueId()); //
    }
}
