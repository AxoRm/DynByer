package me.axorom.dynbyer.gui;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.utils.Item;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static me.axorom.dynbyer.utils.Config.lore;

public class Gui implements Listener {
    private final Inventory inventory;

    public Gui(int size, String title, ArrayList<Item> items, double coefficient) {
        inventory = Bukkit.createInventory(null, size * 9, title);
        initializeItems(items, coefficient);
    }

    public void initializeItems(ArrayList<Item> items, double coefficient) {
        for (Item item : items) {
            inventory.setItem(item.getSlot(), createGuiItem(item.getId(), item.getStartPrice(), coefficient));
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
        net.minecraft.server.v1_16_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound cost = new NBTTagCompound();
        cost.setDouble("cost", coefficient*price);
        assert meta != null;
        meta.setLore(Arrays.asList(lore + price));
        item.setItemMeta(meta);
        return item;
    }

    public void displayInventory(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inventory))
            return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir())
            return;
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
