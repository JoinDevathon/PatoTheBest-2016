package org.devathon.contest2016;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.EnumSet;

public class Constants {

    public static final File PLUGIN_DIR = new File("plugins/Devathon/");
    public static final File QUARRIES_DIR = new File("plugins/Devathon/quarries/");
    public static final String QUARRY_TITLE = ChatColor.GOLD + "Quarry";
    public static final ItemStack QUARRY_ITEM;
    public static final ShapedRecipe QUARRY_RECIPE;

    static {
        ItemStack quarryItem = new ItemStack(Material.DISPENSER);
        ItemMeta im = quarryItem.getItemMeta();
        im.setDisplayName(QUARRY_TITLE);
        quarryItem.setItemMeta(im);
        QUARRY_ITEM = quarryItem;

        QUARRY_RECIPE = new ShapedRecipe(QUARRY_ITEM);
        QUARRY_RECIPE.shape("III", "PDP", "TPT");
        QUARRY_RECIPE.setIngredient('I', Material.IRON_BLOCK);
        QUARRY_RECIPE.setIngredient('P', Material.PISTON_BASE);
        QUARRY_RECIPE.setIngredient('D', Material.DISPENSER);
        QUARRY_RECIPE.setIngredient('T', Material.REDSTONE_TORCH_ON);
    }

    public static  <E extends Enum<E>> E getEnumValueFromString(Class<E> enumClass, String enumString){
        for(E enumElement : EnumSet.allOf(enumClass)){
            if(enumElement.name().equalsIgnoreCase(enumString)){
                return enumElement;
            }
        }

        return null;
    }

}
