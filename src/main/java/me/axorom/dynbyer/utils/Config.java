package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Config {
    static DynByer plugin = DynByer.instance;
    private static final FileConfiguration config = DynByer.instance.getConfig();
    public static String lore;
    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        plugin.saveDefaultConfig();
        lore = config.getString("lore");
        ConfigurationSection section = config.getConfigurationSection("sell.items");
        assert section != null;
        section.getKeys(false).forEach(item -> {
            items.add(new Item(section.getString(item + ".id"), section.getInt(item + ".startprice"), section.getDouble(item + ".coefficient"), section.getInt(item + ".period"), section.getInt(item + ".slot")));
        });
        return items;
    }

    public static int getRows() {
        return config.getInt("rows");
    }

    public static String getTitle(){
        return config.getString("title");
    }
}
