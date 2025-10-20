package com.gylderia.polis.commands;

import com.gylderia.polis.Polis;
import com.gylderia.polis.models.GylderiaChunk;
import com.gylderia.polis.models.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class chunkInfo extends BaseCommand {
    public chunkInfo(Polis plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {
                org.bukkit.Chunk bukkitChunk = player.getLocation().getChunk();
                if (chunkManager.isChunkClaimed(bukkitChunk)) {
                    GylderiaChunk gylderiaChunk = cacheManager.getChunk(bukkitChunk.getChunkKey());
                    Town town = gylderiaChunk.getTown();
                    player.sendMessage("This chunk is claimed by " + town.getName() + "!");
                } else {
                    player.sendMessage("This chunk is not claimed!");
                }
            } else {
                commandSender.sendMessage("Usage: /chunkinfo");
            }
        }
        return true;
    }
}

