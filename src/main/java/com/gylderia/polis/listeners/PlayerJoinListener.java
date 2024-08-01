package com.gylderia.polis.listeners;

import com.gylderia.polis.PlayerManager;
import com.gylderia.polis.Polis;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final PlayerManager playerManager;

    public PlayerJoinListener(Polis plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //debug message

        System.out.println("Player " + player.getName() + " joined the server.");
        if (!playerManager.isPlayerInDatabase(player.getUniqueId())) {
            //debug message
            System.out.println("Player " + player.getName() + " is not in the database.");
            playerManager.createDefaultPlayer(player);
        } else {
            //debug message
            System.out.println("Player " + player.getName() + " is in the database.");
            //get player's data from the database
            playerManager.loadPlayer(player);
        }


    }
}