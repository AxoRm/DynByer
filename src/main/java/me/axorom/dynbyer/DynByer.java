package me.axorom.dynbyer;

import me.axorom.dynbyer.commands.SellerCommand;
import me.axorom.dynbyer.utils.Config;
import me.axorom.dynbyer.utils.Database;
import me.axorom.dynbyer.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class DynByer extends JavaPlugin {
    public static Database database;
    public static DynByer instance;
    public static ArrayList<Item> items;
    @Override
    public void onEnable() {
        instance = this;
        items = Config.getItems();
        new SellerCommand();
        database = new Database();
        Bukkit.getLogger().info("DynBuyer is enabled");
    }

    @Override
    public void onDisable() {
        database.save();
        Bukkit.getLogger().info("DynBuyer is disabled");
    }
}
