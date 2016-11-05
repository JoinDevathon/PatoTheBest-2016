package org.devathon.contest2016.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.devathon.contest2016.quarry.QuarryRegion;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    private final Map<String, QuarryRegion> regions;

    public PlayerListener() {
        this.regions = new HashMap<>();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!regions.containsKey(event.getPlayer().getName())) {
            regions.put(event.getPlayer().getName(), new QuarryRegion());
        }

        if(event.getPlayer().getItemInHand() == null) {
            return;
        }

        if(event.getPlayer().getItemInHand().getType() != Material.BRICK) {
            return;
        }

        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            regions.get(event.getPlayer().getName()).setLocation1(event.getClickedBlock().getLocation());
            event.getPlayer().sendMessage(ChatColor.GREEN + "Point 1 set!");
            event.setCancelled(true);
        }

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            regions.get(event.getPlayer().getName()).setLocation2(event.getClickedBlock().getLocation());
            event.getPlayer().sendMessage(ChatColor.GREEN + "Point 2 set!");
            event.setCancelled(true);
        }
    }

    public QuarryRegion getRegion(Player player) {
        return regions.get(player.getName());
    }
}
