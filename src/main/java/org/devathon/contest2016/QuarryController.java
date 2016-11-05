package org.devathon.contest2016;

import org.devathon.contest2016.quarry.Quarry;

import java.util.ArrayList;
import java.util.List;

public class QuarryController implements Runnable {

    private final DevathonPlugin plugin;
    private final List<Quarry> quarries;

    public QuarryController(DevathonPlugin plugin) {
        this.plugin = plugin;
        this.quarries = new ArrayList<>();
    }

    @Override
    public void run() {
        quarries.forEach(Quarry::run);
    }

    public List<Quarry> getQuarries() {
        return quarries;
    }
}
