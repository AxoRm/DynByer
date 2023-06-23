package me.axorom.dynbyer.gui;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.economy.EconomyUtils;
import me.axorom.dynbyer.inventory.FontItem;
import me.axorom.dynbyer.inventory.Item;
import me.axorom.dynbyer.utils.Config;
import me.axorom.dynbyer.utils.Database;
import me.axorom.dynbyer.utils.DatabaseItem;
import me.axorom.dynbyer.utils.TimeTranslation;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Gui implements Listener {
    private final List<Material> fontMaterials = new ArrayList<>();
    private final int size;
    public static boolean refresh = false;
    private final List<FontItem> updateItems = new ArrayList<>();
    private final Inventory inventory;
    private final EconomyUtils economy = DynByer.economyUtils;
    private final ArrayList<Integer> slots = new ArrayList<>();
    ArrayList<Item> items;
    public Gui(int size, String title, ArrayList<Item> items, Player player) {
        this.size = size;
        inventory = Bukkit.createInventory(null, size * 9, Config.formatForString(title));
        this.items = items;
        initializeItems(player);
        initializeFont(Config.getFontitems(), player);
        Bukkit.getPluginManager().registerEvents(this, DynByer.instance);
    }

    private void initializeFont(ArrayList<FontItem> items, Player player) {
        for(FontItem item : items) {
            String lore = item.getLore();
            String name = item.getName();
            if (lore == "") {
                lore = " ";
            }
            if (name == "") {
                name = " ";
            }
            String material = item.getMaterial();
            if (lore.contains("{0}")) {
                for (int slot : item.getSlots()) {
                    ItemStack itemStack = createSimpleGuiIem(material, name, lore);
                    inventory.setItem(slot, itemStack);
                    ItemMeta meta = itemStack.getItemMeta();
                    String finalLore = lore;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            assert meta != null;
                            if (refresh) {
                                initializeItems(player);
                                refresh = false;
                            }
                            meta.setLore(Config.format(finalLore, TimeTranslation.getDurationBreakdown(getRemainedTime())));
                            itemStack.setItemMeta(meta);
                            inventory.setItem(slot, itemStack);
                        }
                    }.runTaskTimer(DynByer.instance, 0, 20);
                }
                continue;
            }
            if ((lore.length() - lore.replace("%", "").length()) % 2 == 0) {
                updateItems.add(item);
                updateUpdateItems(player);
                continue;
            }
            for (int slot : item.getSlots()) {
                inventory.setItem(slot, createSimpleGuiIem(material, name, lore));
            }
        }
    }

    protected void updateUpdateItems(Player player) {
        for (FontItem item : updateItems) {
            for (int slot : item.getSlots()) {
                String lore = item.getLore();
                if (lore.equals(""))
                    lore = " ";
                lore = PlaceholderAPI.setPlaceholders(player, lore);
                String name = item.getName();
                if (name.equals(""))
                    name = " ";
                String material = item.getMaterial();
                inventory.setItem(slot, createSimpleGuiIem(material, name, lore));
            }
        }
    }

    private int getRemainedTime() {
        int remained = (int) ((DynByer.database.reloadTime - System.currentTimeMillis()));
        return ((Math.abs(remained) + remained)/2);
    }

    public Map<String, Integer> getSelledCount(Player player) {
        Map<String, DatabaseItem> databaseItem = Database.databaseItems.getOrDefault(player.getName(), new HashMap<>());
        Map<String, Integer> coefficients = new HashMap<>();
        databaseItem.forEach((k, item) -> coefficients.put(item.getMaterial(), item.getSelled()));
        return coefficients;
    }

    public double getCoefficient(Player player, Item item) {
        return Math.pow(item.getCoefficient(), 1 + Math.floor((double) getSelledCount(player).getOrDefault(item.getId() + "_" + item.getSlot(), 0) / item.getPeriod()));
    }

    public void initializeItems(Player player) {
        items = DynByer.items;
        for (int slot : slots) {
            inventory.setItem(slot, null);
        }
        for (Item item : items) {
            inventory.setItem(item.getSlot(), createGuiItem(item.getId(), item.getStartPrice(), getCoefficient(player, item), item.getPeriod(), item.getCoefficient()));
            slots.add(item.getSlot());
        }
    }


    protected ItemStack createGuiItem(String materialText, int price, double coefficient, int period, double standartCoefficient) {
        Material material = Material.matchMaterial(materialText);
        if (material == null) {
            Bukkit.getLogger().info(DynByer.messages.format(DynByer.messages.getString("invalidMaterial"), materialText));
            DynByer.instance.getServer().getPluginManager().disablePlugin(DynByer.instance);
            return null;
        }
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        PersistentDataContainer container =  meta.getPersistentDataContainer();
        container.set(new NamespacedKey(DynByer.instance, "price"), PersistentDataType.DOUBLE, coefficient*price);
        meta.setLore(Config.format(Config.lore, String.valueOf(String.format(Locale.US, "%,.2f", coefficient*price)), String.valueOf( String.format(Locale.US,"%,.2f",(price * coefficient * (1 - Math.pow(standartCoefficient, 64 / period))) / (1 - standartCoefficient)))));
        item.setItemMeta(meta);
        return item;
    }

    protected ItemStack createSimpleGuiIem(String materialText, String name, String lore) {
        Material material = Material.matchMaterial(materialText);
        fontMaterials.add(material);
        if (material == null) {
            Bukkit.getLogger().info(DynByer.messages.format(DynByer.messages.getString("invalidMaterial"), materialText));
            DynByer.instance.getServer().getPluginManager().disablePlugin(DynByer.instance);
            return null;
        }
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Config.formatForString(name));
        meta.setLocalizedName(Config.formatForString(name));
        meta.setLore(Config.format(lore, TimeTranslation.getDurationBreakdown(getRemainedTime())));
        item.setItemMeta(meta);
        return item;
    }

    public void displayInventory(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!Objects.equals(e.getClickedInventory(), inventory)) return;
        final Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        initializeItems(player);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir() || fontMaterials.contains(clickedItem.getType())) return;
        Item item = DynByer.items.stream().filter(aitem -> aitem.getSlot() == e.getSlot()).findFirst().orElse(new Item("cobblestone",0,0,0,0));
        Map<String, DatabaseItem> databaseItems = Database.databaseItems.getOrDefault(player.getName(), new HashMap<>());
        DatabaseItem databaseItem = databaseItems.getOrDefault(item.getId() + "_" + e.getSlot(), new DatabaseItem(0, item.getId() + "_" + e.getSlot()));
        if (!e.isLeftClick())
            return;
        if (e.isShiftClick()) {
            if (!economy.sellStackItem(player, item.getStartPrice() * getCoefficient(player, item), Material.matchMaterial(item.getId()), item.getCoefficient(), item.getPeriod(), databaseItem)) {
                player.sendMessage(DynByer.messages.getString("notEnoughBlocks"));
                return;
            }
        } else {
            if (!economy.sellItem(player, item.getStartPrice() * getCoefficient(player, item), Material.matchMaterial(item.getId()))) {
                player.sendMessage(DynByer.messages.getString("notEnoughBlocks"));
                return;
            }
            databaseItem.addSelled(1);
        }
        updateUpdateItems(player);
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
