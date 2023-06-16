package me.axorom.dynbyer.gui;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.economy.EconomyUtils;
import me.axorom.dynbyer.utils.Config;
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

public class Gui implements Listener {
    private final Inventory inventory;
    private final EconomyUtils economy = DynByer.economyUtils;
    ArrayList<Item> items;
    public Gui(int size, String title, ArrayList<Item> items, Player player) {
        inventory = Bukkit.createInventory(null, size * 9, title);
        this.items = items;
        initializeItems(player);
        Bukkit.getPluginManager().registerEvents(this, DynByer.instance);
    }

    public Map<String, Integer> getCoefficients(Player player) {
        Map<String, DatabaseItem> databaseItem = Database.databaseItems.getOrDefault(player.getName(), new HashMap<>());
        Map<String, Integer> coefficients = new HashMap<>();
        databaseItem.forEach((k, item) -> coefficients.put(item.getMaterial(), item.getSelled()));
        return coefficients;
    }

    public double getCoefficient(Player player, Item item) {
        return Math.pow(item.getCoefficient(), Math.floor((double) getCoefficients(player).getOrDefault(item.getId() + "_" + item.getSlot(), 0) / item.getPeriod()));
    }

    public void initializeItems(Player player) {
        for (Item item : items) {
            inventory.setItem(item.getSlot(), createGuiItem(item.getId(), item.getStartPrice(), getCoefficient(player, item)));
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
        assert meta != null;
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
        final Player player = (Player) e.getWhoClicked();
        Item item = DynByer.items.stream().filter(aitem -> aitem.getSlot() == e.getSlot()).findFirst().orElse(new Item("cobblestone",0,0,0,0));
        Map<String, DatabaseItem> databaseItems = Database.databaseItems.getOrDefault(player.getName(), new HashMap<>());
        DatabaseItem databaseItem = databaseItems.getOrDefault(item.getId() + "_" + e.getSlot(), new DatabaseItem(0, item.getId() + "_" + e.getSlot()));
        if (e.isShiftClick()) {
            if (!economy.sellStackItem(player, item.getStartPrice() * getCoefficient(player, item), Material.matchMaterial(item.getId()), item.getCoefficient(), item.getPeriod())) {
                player.sendMessage(ChatColor.RED + "Недостаточно блоков в инвентаре");
                return;
            }
            databaseItem.addSelled(64);
        } else {
            if (!economy.sellItem(player, item.getStartPrice() * getCoefficient(player, item), Material.matchMaterial(item.getId()))) {
                player.sendMessage(ChatColor.RED + "Недостаточно блоков в инвентаре");
                return;
            }
            databaseItem.addSelled(1);
        }
        databaseItems.put(item.getId() + "_" + e.getSlot(), databaseItem);
        Database.databaseItems.put(player.getName(), databaseItems);
        initializeItems(player);
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }
}
