package org.devathon.contest2016.quarry;

import org.bukkit.ChatColor;

public enum Status {

    MINING(ChatColor.GREEN + "Quarry is mining"),
    NO_CHEST(ChatColor.RED + "Quarry is missing the storage chest"),
    STORAGE_FULL(ChatColor.RED + "Storage is full"),
    FINISHED(ChatColor.GREEN + "Quarry has finished"),
    OUT_OF_FUEL(ChatColor.RED + "Out of fuel"),
    OTHER(ChatColor.RED + "Error");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
