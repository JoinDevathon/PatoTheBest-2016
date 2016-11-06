package org.devathon.contest2016.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.quarry.Quarry;
import org.devathon.contest2016.quarry.QuarryRegion;
import org.devathon.contest2016.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerListener implements Listener {

    private final DevathonPlugin plugin;

    public PlayerListener(DevathonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShiftClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(event.getClickedBlock().getType() != Material.DISPENSER) {
            return;
        }

        Dispenser dispenser = (Dispenser) event.getClickedBlock().getState();
        if(dispenser.getInventory().getTitle() == null) {
            return;
        }

        if(!dispenser.getInventory().getTitle().equals(Constants.QUARRY_TITLE)) {
            return;
        }

        if(plugin.getQuarryController().getFromQuarryBlock(event.getClickedBlock()) != null) {
            Quarry quarry = plugin.getQuarryController().getFromQuarryBlock(event.getClickedBlock());
            event.getPlayer().sendMessage(quarry.getStatus());
            return;
        }

        if(!event.getPlayer().isSneaking()) {
            return;
        }

        event.setCancelled(true);
        for(BlockFace blockFace : BlockFace.values()) {
            if(event.getClickedBlock().getRelative(blockFace).getType() == Material.REDSTONE_TORCH_ON) {
                Block torch1 = event.getClickedBlock().getRelative(blockFace);
                List<Block> torches = new ArrayList<>();
                torches.add(findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX()+1, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ(), torch1.getLocation().getBlockX()+65, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()));
                torches.add(findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX()-65, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ(), torch1.getLocation().getBlockX()-1, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()));
                torches.add(findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()+1, torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()+65));
                torches.add(findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()-65, torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()-1));
                //noinspection SuspiciousMethodCalls
                torches.removeAll(Collections.singleton(null));

                if(torches.size() != 2) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Boundaries not found!");
                    return;
                }

                QuarryRegion region = new QuarryRegion();
                region.setLocation1(torches.get(0).getLocation());
                region.setLocation2(torches.get(1).getLocation().clone().add(0, 5, 0));

                Quarry quarry = region.createQuarry(event.getClickedBlock());
                plugin.getQuarryController().addQuarry(quarry);
                event.getPlayer().sendMessage(ChatColor.GREEN + "Started mining at " + quarry.getBlock().getX() + ", " + quarry.getBlock().getY() + ", " + quarry.getBlock().getZ());
            }
        }
    }

    private Block findTorch(World world, int startx, int starty, int startz, int endx, int endy, int endz) {
        for(int x = startx; x <= endx; x++) {
            for(int y = starty; y <= endy; y++) {
                for(int z = startz; z <= endz; z++) {
                    if(world.getBlockAt(x, y, z).getType() == Material.REDSTONE_TORCH_ON) {
                        return world.getBlockAt(x, y, z);
                    }
                }
            }
        }

        return null;
    }
}
