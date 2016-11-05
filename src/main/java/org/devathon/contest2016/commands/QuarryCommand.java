package org.devathon.contest2016.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.quarry.Quarry;
import org.devathon.contest2016.quarry.QuarryRegion;

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

        Player player = (Player) commandSender;
        QuarryRegion region = plugin.getPlayerListener().getRegion(player);

        if(!region.isValid()) {
            player.sendMessage(ChatColor.RED + "Please select two points using a brick.");
            return false;
        }

        Quarry quarry = region.createQuarry();
        Bukkit.getScheduler().runTaskTimer(plugin, quarry, 0L, 10L);
        player.sendMessage(ChatColor.GREEN + "Quarry created!");
        return false;
    }
}
