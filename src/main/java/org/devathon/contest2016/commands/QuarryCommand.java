package org.devathon.contest2016.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.devathon.contest2016.DevathonPlugin;

public class QuarryCommand implements CommandExecutor {

    private final DevathonPlugin plugin;

    public QuarryCommand(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "I'm sorry, this command is only available for players.");
            return false;
        }


        return false;
    }
}
