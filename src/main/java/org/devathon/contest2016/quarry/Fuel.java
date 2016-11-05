package org.devathon.contest2016.quarry;

import org.bukkit.Material;

public enum Fuel {

    COAL(Material.COAL, 64),
    COAL_BLOCK(Material.COAL_BLOCK, 576),
    LAVA_BUCKET(Material.LAVA_BUCKET, 800),
    BLAZE_ROD(Material.BLAZE_ROD, 96),
    WOOD(Material.WOOD, 8),
    LOG(Material.LOG, 32);

    private final Material fuelMaterial;
    private final int fuel;

    Fuel(Material fuelMaterial, int fuel) {
        this.fuelMaterial = fuelMaterial;
        this.fuel = fuel;
    }

    public Material getFuelMaterial() {
        return fuelMaterial;
    }

    public int getFuel() {
        return fuel;
    }
}
