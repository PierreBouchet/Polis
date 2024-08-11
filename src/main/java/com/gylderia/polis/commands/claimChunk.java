package com.gylderia.polis.commands;

import com.gylderia.polis.Polis;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class claimChunk extends BaseCommand {


    public claimChunk(Polis plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("polis.chunk.claim")) {
                if (strings.length == 0) {
                    if (!chunkManager.isChunkClaimed(player.getLocation().getChunk())) {
                        if (cacheManager.getPlayer(player.getUniqueId()).hasTown()) {
                                chunkManager.claimChunk(player.getLocation().getChunk(), cacheManager.getPlayer(player.getUniqueId()).getTown());
                                player.sendMessage("Chunk claimed!");
                            }
                        } else {
                            player.sendMessage("You are not in a town!");
                        }
                    } else {
                        player.sendMessage("This chunk is already claimed!");
                    }
                } else {
                    player.sendMessage("Usage: /claimchunk");
                }
        }


        return false;
    }
}
