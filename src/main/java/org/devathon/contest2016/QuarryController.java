package org.devathon.contest2016;

import org.devathon.contest2016.quarry.Quarry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.devathon.contest2016.Constants.QUARRIES_DIR;

public class QuarryController implements Runnable {

    private final DevathonPlugin plugin;
    private final List<Quarry> quarries;

    public QuarryController(DevathonPlugin plugin) {
        this.plugin = plugin;
        this.quarries = new ArrayList<>();
    }

    public void loadQuarries() {
        if(!QUARRIES_DIR.exists()) {
            QUARRIES_DIR.mkdirs();
        }

        if(QUARRIES_DIR.listFiles() == null) {
            return;
        }

        for (File file : QUARRIES_DIR.listFiles()) {
            addQuarry(new Quarry(file.getName()));
        }
    }

    public void addQuarry(Quarry quarry) {
        quarries.add(quarry);
        quarry.runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void run() {
        quarries.forEach(Quarry::run);
    }

    public List<Quarry> getQuarries() {
        return quarries;
    }

    public void saveQuarries() {
        System.out.println("saving quarries");
        quarries.forEach(Quarry::save);
        quarries.forEach(Quarry::destroyDrill);
    }
}
