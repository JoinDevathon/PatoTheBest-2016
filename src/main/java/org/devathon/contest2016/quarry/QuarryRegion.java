package org.devathon.contest2016.quarry;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class QuarryRegion {

    private Location location1;
    private Location location2;

    public Location getLocation1() {
        return location1;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public boolean isValid() {
        return location1 != null && location2 != null;
    }

    public Quarry createQuarry() {
        Vector point1 = new Vector(location1.getBlockX() < location2.getBlockX() ? location1.getBlockX() : location2.getBlockX(), location1.getBlockY() < location2.getBlockY() ? location1.getBlockY() : location2.getBlockY(), location1.getBlockZ() < location2.getBlockZ() ? location1.getBlockZ() : location2.getBlockZ());
        Vector point2 = new Vector(location1.getBlockX() >= location2.getBlockX() ? location1.getBlockX() : location2.getBlockX(), location1.getBlockY() >= location2.getBlockY() ? location1.getBlockY() : location2.getBlockY(), location1.getBlockZ() >= location2.getBlockZ() ? location1.getBlockZ() : location2.getBlockZ());
        return new Quarry(location1.getWorld(), point1, point2);
    }
}
