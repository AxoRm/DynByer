package me.axorom.dynbyer.gui;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import me.axorom.dynbyer.utils.Config;

public class Gui implements Listener {
    private final Inventory inventory;

    public Gui(int size, String title, ArrayList<Item> items, Map<String, Double> coefficients) {
        inventory = Bukkit.createInventory(null, size * 9, title);
        initializeItems(items, coefficients);
    }

    public void initializeItems(ArrayList<Item> items, Map<String, Double> coefficients) {
        for (Item item : items) {
            inventory.setItem(item.getSlot(), createGuiItem(item.getId(), item.getStartPrice(), coefficients.getOrDefault(item.getId(), item.getCoefficient())));
        }
    }

    protected ItemStack createGuiItem(String materialText, int price, double coefficient) {
        Material material = Material.matchMaterial(materialText);
        if (material == null) {
            Bukkit.getLogger().info(ChatColor.RED + "Material <" + materialText + "> invalid, please correct the config!");
            DynByer.instance.getServer().getPluginManager().disablePlugin(DynByer.instance);
        }
        final ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(materialText)), 1);
        final ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container =  meta.getPersistentDataContainer();
        container.set(new NamespacedKey(DynByer.instance, "price"), PersistentDataType.DOUBLE, coefficient*price);
        meta.setLore(Arrays.asList(Config.format(Config.lore, String.valueOf(coefficient*price))));
        item.setItemMeta(meta);
        return item;
    }

    public void displayInventory(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inventory)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }
}
