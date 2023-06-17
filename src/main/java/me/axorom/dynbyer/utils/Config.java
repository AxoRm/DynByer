package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.inventory.FontItem;
import me.axorom.dynbyer.inventory.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static ArrayList<FontItem> fontitems;
    private static final FileConfiguration config = DynByer.instance.getConfig();
    public static String lore;
    private int calls;
    private List<String> periods;

    public static Config configClass;
    public Config() {
        getFontItems();
        ConfigurationSection section = config.getConfigurationSection("sell");
        assert section != null;
        periods = new ArrayList<>(section.getKeys(false));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (DynByer.database.reloadTime > System.currentTimeMillis()) return;
                DynByer.database.reloadTime = System.currentTimeMillis()+(config.getInt("time")* 60000L);
                DynByer.database.save();
                DynByer.items = getItems();
            }
        }.runTaskTimer(DynByer.instance, 0, 1200);
    }

    public static Config getConfigClass() {
        return configClass;
    }

    public void reloadConfig() {
        calls = 0;
        ConfigurationSection section = config.getConfigurationSection("sell");
        assert section != null;
        periods = new ArrayList<>(section.getKeys(false));
    }
    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        String period = periods.get(calls);
        lore = config.getString("lore");
        ConfigurationSection section = config.getConfigurationSection("sell."+period+".items");
        Bukkit.getLogger().info("section: " + section);
        assert section != null;
        section.getKeys(false).forEach(item -> {
            Bukkit.getLogger().info("first item: " + item);
            items.add(new Item(section.getString(item + ".id"), section.getInt(item + ".startprice"), section.getDouble(item + ".coefficient"), section.getInt(item + ".period"), section.getInt(item + ".slot")));
            Bukkit.getLogger().info(items.toString());
        });
        if (calls +1 > periods.size())
            calls = 0;
        else
            calls++;
        return items;
    }

    public static ArrayList<FontItem> getFontitems() {
        return fontitems;
    }

    public void getFontItems() {
        fontitems = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("constantmenu");
        assert section != null;
        section.getKeys(false).forEach(fontitem -> fontitems.add(new FontItem(section.getString(fontitem + ".id"), section.getString(fontitem + ".name"), section.getString(fontitem + ".lore"), section.getIntegerList(fontitem + ".slots"))));
    }

    public static int getRows() {
        return config.getInt("rows");
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

    public static List<String> formatForList(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        return Arrays.asList(s.split("\n"));
    }
    public static String formatForString(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
