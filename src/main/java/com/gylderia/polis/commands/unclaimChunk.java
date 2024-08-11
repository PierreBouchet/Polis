package com.gylderia.polis.commands;

import com.gylderia.polis.Polis;
import com.gylderia.polis.models.GylderiaChunk;
import com.gylderia.polis.models.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class unclaimChunk extends BaseCommand {

    public unclaimChunk(Polis plugin) {
        super(plugin);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender.hasPermission("polis.chunk.unclaim")) {
            if (args.length == 0) {
                GylderiaChunk chunk = cacheManager.getChunk(player.getLocation().getChunk().getChunkKey());
                if (chunkManager.isChunkClaimed(chunk)) {
                    if (cacheManager.getPlayer(player.getUniqueId()).hasTown()) {
                        Town town = cacheManager.getPlayer(player.getUniqueId()).getTown();
                        if (town == chunk.getTown()) {
                            chunkManager.unclaimChunk(player.getLocation().getChunk());
                            player.sendMessage("Chunk unclaimed!");
                        } else {
                            player.sendMessage("This chunk is not claimed by your town!");
                        }
                    } else {
                        player.sendMessage("You are not in a town!");
                    }
                } else {
                    player.sendMessage("This chunk is not claimed!");
                }
            } else {
                player.sendMessage("Usage: /unclaimchunk");
            }
        } else {
            player.sendMessage("You do not have permission to execute the /unclaimchunk command!");
        }
        return true;
    }
}
