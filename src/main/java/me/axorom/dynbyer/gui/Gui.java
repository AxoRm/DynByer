package me.axorom.dynbyer.gui;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.utils.Database;
import me.axorom.dynbyer.utils.DatabaseItem;
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

import java.util.*;

import me.axorom.dynbyer.utils.Config;

public class Gui implements Listener {
    private final Inventory inventory;

    public Gui(int size, String title, ArrayList<Item> items, Map<String, Integer> coefficients) {
        inventory = Bukkit.createInventory(null, size * 9, title);
        initializeItems(items, coefficients);
    }

    public void initializeItems(ArrayList<Item> items, Map<String, Integer> coefficients) {
        for (Item item : items) {
            inventory.setItem(item.getSlot(), createGuiItem(item.getId(), item.getStartPrice(), Math.pow(item.getCoefficient(), Math.floor((double) coefficients.getOrDefault(item.getId(), 0) /item.getPeriod()))));
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
        if (!Objects.equals(e.getClickedInventory(), inventory)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();
        p.sendMessage("You clicked at slot " + e.getRawSlot());

        Item item = DynByer.items.stream().filter(aitem -> aitem.getSlot() == e.getSlot()).findFirst().get();



        Map<String, DatabaseItem> databaseItems = Database.databaseItems.getOrDefault(p.getName(), new HashMap<>());
        DatabaseItem databaseItem = databaseItems.getOrDefault(item.getId(), new DatabaseItem(0, item.getId()));
        databaseItem.addSelled(1);
        databaseItems.put(item.getId(), databaseItem);
        Database.databaseItems.put(p.getName(), databaseItems);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }
}
