package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.listener.PlayerListener;
import org.devathon.contest2016.commands.QuarryCommand;

public class DevathonPlugin extends JavaPlugin  {

    private final PlayerListener playerListener;

    public DevathonPlugin() {
        playerListener = new PlayerListener();
    }

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(playerListener, this);
        getCommand("quarry").setExecutor(new QuarryCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }
}

