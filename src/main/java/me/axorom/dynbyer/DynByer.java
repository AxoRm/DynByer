package me.axorom.dynbyer;

import me.axorom.dynbyer.commands.SellerCommand;
import me.axorom.dynbyer.economy.EconomyUtils;
import me.axorom.dynbyer.inventory.Item;
import me.axorom.dynbyer.utils.Config;
import me.axorom.dynbyer.utils.Database;
import me.axorom.dynbyer.utils.MessagesParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class DynByer extends JavaPlugin {
    public static Database database;
    public static DynByer instance;
    public static EconomyUtils economyUtils;
    public static ArrayList<Item> items;
    public static MessagesParser messages;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        messages = new MessagesParser(this);
        Config config = new Config();
        database = new Database();
        items = config.getItems();
        new SellerCommand();
        economyUtils = new EconomyUtils();
        Bukkit.getLogger().info(messages.getString("startup"));
    }

    @Override
    public void onDisable() {
        database.save();
        Bukkit.getLogger().info(messages.getString("end"));
    }
}
