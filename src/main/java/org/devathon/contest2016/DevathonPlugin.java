package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.commands.QuarryCommand;
import org.devathon.contest2016.listener.PlayerListener;

public class DevathonPlugin extends JavaPlugin  {

    private final PlayerListener playerListener;
    private final QuarryController quarryController;

    public DevathonPlugin() {
        this.playerListener = new PlayerListener(this);
        this.quarryController = new QuarryController(this);
    }

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(playerListener, this);
        getCommand("quarry").setExecutor(new QuarryCommand(this));

        quarryController.loadQuarries();
        getServer().addRecipe(Constants.QUARRY_RECIPE);
    }

    @Override
    public void onDisable() {
        quarryController.saveQuarries();
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public QuarryController getQuarryController() {
        return quarryController;
    }
}

