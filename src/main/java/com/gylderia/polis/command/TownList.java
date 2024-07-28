package com.gylderia.polis.command;

import com.gylderia.polis.Towns.Town;
import com.gylderia.polis.data.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;

public class TownList implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        DataManager.TownCache.forEach((key, value) -> {
            commandSender.sendMessage("L'UUID de la ville " + value.getName() + " est " + key);
        });
        return false;
    }
}