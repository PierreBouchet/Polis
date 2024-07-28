package com.gylderia.polis.Events;

import com.gylderia.polis.Towns.PolisPlayer;
import com.gylderia.polis.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.xml.crypto.Data;

public class JoinEvent implements Listener {
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        DataManager.loadPlayerData(e.getPlayer().getUniqueId()); //
    }
}
