package me.axorom.dynbyer.economy;

import me.axorom.dynbyer.DynByer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;

public class EconomyUtils {
    private Economy economy;
    DynByer plugin = DynByer.instance;

    public Economy getEconomy() {
        return economy;
    }

    public EconomyUtils() {
        boolean check = setupEconomy();
        if (check)
            Bukkit.getLogger().info(DynByer.messages.getString("economyTrue"));
        else
            Bukkit.getLogger().info(DynByer.messages.getString("economyFalse"));
    }
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
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
        for (int i = 0; i < 36; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item == null)
                continue;
            if (item.getType() != material)
                continue;
            item.setAmount(item.getAmount() - 1);
            economy.depositPlayer(player, price);
            player.sendMessage(DynByer.messages.formatPlaceholder(player, DynByer.messages.getString("gain"), String.format("%,.2f", price), "1"));
            return true;
        }
        return false;
    }

    public boolean sellStackItem(Player player, double price, Material material, double coefficient, int period) {
        int stack = 64;
        int halfstack = 32;
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
        if (count <= 16) {
            return false;
        } else if (count < halfstack + 1) {
            pay(indexes, 16, playerInventory, player, price, coefficient, period);
            return true;
        } else if (count < stack + 1) {
            pay(indexes, 32, playerInventory, player, price, coefficient, period);
            return true;
        } else {
            pay(indexes, 64, playerInventory, player, price, coefficient, period);
            return true;
        }
    }

    public void pay(ArrayList<Integer> indexes, int size, Inventory playerInventory, Player player, double price, double coefficient, int period) {
        int sizetemp = size;
        for (int i : indexes) {
            ItemStack item = playerInventory.getItem(i);
            if (item == null)
                continue;
            int amount = item.getAmount();
            if (amount <= size) {
                size -= amount;
                item.setAmount(0);
            } else {
                item.setAmount(amount - size);
                size = 0;
            }
            playerInventory.setItem(i, item);
            if (size <= 0) {
                economy.depositPlayer(player, (price * (1 - Math.pow(coefficient, size / period))) / (1 - coefficient));
            }
        }
        player.sendMessage(DynByer.messages.formatPlaceholder(player, DynByer.messages.getString("gain"), String.format("%,.2f", (price * (1 - Math.pow(coefficient, size / period))) / (1 - coefficient)), String.valueOf(sizetemp)));
    }
}