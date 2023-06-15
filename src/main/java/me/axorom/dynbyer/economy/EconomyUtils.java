package me.axorom.dynbyer.economy;

import me.axorom.dynbyer.DynByer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.logging.Level;

public class EconomyUtils {
    private Economy economy;
    DynByer plugin = DynByer.instance;

    public Economy getEconomy() {
        return economy;
    }

    public EconomyUtils() {
        boolean check = setupEconomy();
        if (check)
            Bukkit.getLogger().info(ChatColor.GREEN + "Economy linked with DynBuyer plugin");
        else
            Bukkit.getLogger().info(ChatColor.RED + "Economy not linked with DynBuyer plugin");
    }
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().log(Level.SEVERE, "NO RSP");
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public boolean buyItem(Player player, double price) {
        if (economy.getBalance(player) < price)
            return false;
        economy.withdrawPlayer(player, price);
        return true;
    }

    public boolean sellItem(Player player, double price, Material material) {
        Inventory playerInventory = player.getInventory();
        int count = 0;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item == null)
                continue;
            if (item.getType() != material)
                continue;
            count += item.getAmount();
            indexes.add(i);
        }
        if (count < )
        economy.depositPlayer(player, price);
    }
    public boolean sellStackItem(Player player, double price, Material material) {
}