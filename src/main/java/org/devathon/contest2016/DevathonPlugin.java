package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.controller.QuarryController;
import org.devathon.contest2016.listener.DevatonPlayer;
import org.devathon.contest2016.listener.PlayerListener;
import org.devathon.contest2016.util.Constants;

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
        getServer().getPluginManager().registerEvents(new DevatonPlayer(), this);

        quarryController.loadQuarries();
        getServer().addRecipe(Constants.QUARRY_RECIPE);
    }

    @Override
    public void onDisable() {
        quarryController.saveQuarries();
    }

    public QuarryController getQuarryController() {
        return quarryController;
    }
}

