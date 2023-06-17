package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Config {
    static DynByer plugin = DynByer.instance;
    private static final FileConfiguration config = DynByer.instance.getConfig();
    public static String lore;
    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        lore = config.getString("lore");
        ConfigurationSection section = config.getConfigurationSection("sell.items");
        Bukkit.getLogger().info("section: " + section);
        assert section != null;
        section.getKeys(false).forEach(item -> {
            Bukkit.getLogger().info("first item: " + item);
            items.add(new Item(section.getString(item + ".id"), section.getInt(item + ".startprice"), section.getDouble(item + ".coefficient"), section.getInt(item + ".period"), section.getInt(item + ".slot")));
            Bukkit.getLogger().info(items.toString());
        });
        return items;
    }

    public static int getRows() {
        return config.getInt("rows");
    }
    public static String getFont() {
        return config.getString("empty");
    }

    public static String getTitle(){
        return config.getString("title");
    }

    public static String format(String join, String ... s) {
        for (int i = 0; i < s.length; i++) {
            join = join.replaceAll("\\{"+i+"}", s[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', join);
    }
}
