package org.devathon.contest2016.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.devathon.contest2016.Constants;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.quarry.Quarry;
import org.devathon.contest2016.quarry.QuarryRegion;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    private final Map<String, QuarryRegion> regions;
    private DevathonPlugin plugin;

    public PlayerListener(DevathonPlugin plugin) {
        this.regions = new HashMap<>();
        this.plugin = plugin;
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

    @EventHandler
    public void onShiftClick(PlayerInteractEvent event) {
        if(!event.getPlayer().isSneaking()) {
            return;
        }

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

        event.setCancelled(true);
        for(BlockFace blockFace : BlockFace.values()) {
            if(event.getClickedBlock().getRelative(blockFace).getType() == Material.REDSTONE_TORCH_ON) {
                Block torch1 = event.getClickedBlock().getRelative(blockFace);
                Block[] torches = new Block[2];
                Block torch2 = findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX()+1, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ(), torch1.getLocation().getBlockX()+65, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ());
                Block torch3 = findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX()-65, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ(), torch1.getLocation().getBlockX()-1, torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ());
                Block torch4 = findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()+1, torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()+65);
                Block torch5 = findTorch(torch1.getLocation().getWorld(), torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()-65, torch1.getLocation().getBlockX(), torch1.getLocation().getBlockY(), torch1.getLocation().getBlockZ()-1);

                if(torch2 != null) {
                    torches[0] = torch2;
                }

                if(torch3 != null) {
                    if(torches[0] == null) {
                        torches[0] = torch3;
                    } else {
                        torches[1] = torch3;
                    }
                }

                if(torch4 != null) {
                    if(torches[0] == null) {
                        torches[0] = torch4;
                    } else {
                        torches[1] = torch4;
                    }
                }

                if(torch5 != null) {
                    if(torches[0] == null) {
                        torches[0] = torch5;
                    } else {
                        torches[1] = torch5;
                    }
                }

                if(torches[1] == null) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Boundaries not found!");
                    return;
                }

                QuarryRegion region = new QuarryRegion();
                region.setLocation1(torches[0].getLocation());
                region.setLocation2(torches[1].getLocation().clone().add(0, 5, 0));

                Quarry quarry = region.createQuarry();
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

    public QuarryRegion getRegion(Player player) {
        return regions.get(player.getName());
    }
}
