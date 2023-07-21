package me.baraban4ik.ecosalary.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Items {
    public static ItemStack createItem(String material, int amount, String name, List<String> lore) {
        Material item_mat = Material.getMaterial(material);
        if (item_mat == null) {
            return null;
        }
        ItemStack item = new ItemStack(item_mat, amount);
        ItemMeta item_meta = item.getItemMeta();

        item_meta.setDisplayName(name);
        item_meta.setLore(lore);

        item.setItemMeta(item_meta);

        return item;
    }
}
