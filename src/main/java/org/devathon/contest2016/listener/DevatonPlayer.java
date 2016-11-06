package org.devathon.contest2016.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.devathon.contest2016.util.Constants;

public class DevatonPlayer implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().clear();

        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setDisplayName(ChatColor.GREEN + "Information");
        bookMeta.setAuthor("Pato_the_best");
        bookMeta.setTitle(ChatColor.GREEN + "Information");
        bookMeta.addPage(ChatColor.BOLD + "Welcome!\n" + ChatColor.RESET +
                "\n" +
                "This is a Quarry remake from the popular mod BuildCraft using purely vanilla stuff. This was made in less than 24 hours as the submission for the Devathon contest.\n" +
                "\n");
        bookMeta.addPage("In your inventory you have a set of items. If you press E, you will see the crafting recipe for the Quarry on the left and the result on the right.\n");
        bookMeta.addPage(ChatColor.UNDERLINE + "How to Setup\n" + ChatColor.RESET +
                ChatColor.BOLD + "1) " + ChatColor.BLACK + "Place the Quarry block down wherever you want\n" +
                ChatColor.BOLD + "2) " + ChatColor.BLACK + "Using 4 redstone torches, mark an area where the quarry will work (must be on the same height, must form a perfect rectangle, and one of the torches must be touching the quarry block)\n");
        bookMeta.addPage(ChatColor.BOLD + "3) " + ChatColor.BLACK + "Place a chest on any side of the Quarry block.\n" +
                ChatColor.BOLD + "4) " + ChatColor.BLACK + "Shift right-click the Quarry block in order to create the Quarry structure\n" +
                ChatColor.BOLD + "5) " + ChatColor.BLACK + "Place some fuel inside the Quarry Block and let the magic begin\n");
        bookMeta.addPage(ChatColor.UNDERLINE + "Fuels\n" + ChatColor.RESET +
                "The Quarry must be supplied with fuels in order for it to work\n" +
                "Below its a table with the different fuels the quarry can use\n" +
                "\n" +
                "Each fuel item will allow the quarry to mine a certain amount of blocks" +
                "\n");
        bookMeta.addPage("Coal = 64 blocks\n" +
                        "Coal Block = 576 blocks\n" +
                        "Lava Bucket = 800 blocks\n" +
                        "Blaze Rod = 96 blocks\n" +
                        "Wood = 8 blocks\n" +
                        "Log = 32 blocks");
        bookMeta.addPage("This plugin is persistent through reloads meaning that the quarries won't disappear every time you reload\n" +
                "\nThanks for organizing this contest!" +
                "\n" +
                "\nKind Regards," +
                "\n-Pato");
        itemStack.setItemMeta(bookMeta);
        event.getPlayer().getInventory().setItem(0, itemStack);
        event.getPlayer().getInventory().setItem(1, Constants.QUARRY_ITEM);
        event.getPlayer().getInventory().setItem(2, new ItemStack(Material.REDSTONE_TORCH_ON, 4));
        event.getPlayer().getInventory().setItem(3, new ItemStack(Material.CHEST, 1));

        event.getPlayer().getInventory().setItem(10, new ItemStack(Material.IRON_BLOCK, 1));
        event.getPlayer().getInventory().setItem(11, new ItemStack(Material.IRON_BLOCK, 1));
        event.getPlayer().getInventory().setItem(12, new ItemStack(Material.IRON_BLOCK, 1));
        event.getPlayer().getInventory().setItem(19, new ItemStack(Material.PISTON_BASE, 1));
        event.getPlayer().getInventory().setItem(20, new ItemStack(Material.DISPENSER, 1));
        event.getPlayer().getInventory().setItem(21, new ItemStack(Material.PISTON_BASE, 1));
        event.getPlayer().getInventory().setItem(28, new ItemStack(Material.REDSTONE_TORCH_ON, 1));
        event.getPlayer().getInventory().setItem(29, new ItemStack(Material.PISTON_BASE, 1));
        event.getPlayer().getInventory().setItem(30, new ItemStack(Material.REDSTONE_TORCH_ON, 1));

        event.getPlayer().getInventory().setItem(24, Constants.QUARRY_ITEM);
    }

}
