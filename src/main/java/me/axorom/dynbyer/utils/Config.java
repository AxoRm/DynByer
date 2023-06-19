package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.gui.Gui;
import me.axorom.dynbyer.inventory.FontItem;
import me.axorom.dynbyer.inventory.Item;
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
    private List<String> periods;

    public static Config configClass;
    public Config() {
        getFontItems();
        ConfigurationSection section = config.getConfigurationSection("sell");
        assert section != null;
        configClass = this;
        periods = new ArrayList<>(section.getKeys(false));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (DynByer.database.frequency - 1000L <= System.currentTimeMillis()) {
                    DynByer.database.frequency = System.currentTimeMillis()+(config.getInt("frequency")* 60000L);
                    DynByer.database.resetBase();
                    DynByer.items = getItems();
                    Gui.refresh = true;
                }
                if (DynByer.database.reloadTime - 1000L > System.currentTimeMillis()) return;
                DynByer.database.reloadTime = System.currentTimeMillis()+(config.getInt("time")* 60000L);
                DynByer.database.save();
                if (DynByer.database.calls + 1 == periods.size())
                    DynByer.database.calls = 0;
                else
                    DynByer.database.calls++;
                DynByer.items = getItems();
                Gui.refresh = true;
            }
        }.runTaskTimer(DynByer.instance, 0, 1200);
    }
    public static Config getConfigClass() {
        return configClass;
    }

    public void reloadConfig() {
        DynByer.database.reloadBase();
        DynByer.items = getItems();
        getFontItems();
        DynByer.messages.reloadConfig();
        ConfigurationSection section = config.getConfigurationSection("sell");
        assert section != null;
        periods = new ArrayList<>(section.getKeys(false));
    }
    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        String period = periods.get(DynByer.database.calls);
        lore = config.getString("lore");
        ConfigurationSection section = config.getConfigurationSection("sell."+period+".items");
        assert section != null;
        section.getKeys(false).forEach(item -> items.add(new Item(section.getString(item + ".id"), section.getInt(item + ".startprice"),
                section.getDouble(item + ".coefficient"), section.getInt(item + ".period"), section.getInt(item + ".slot"))));
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

    public static List<String> format(String join, String ... s) {
        for (int i = 0; i < s.length; i++) {
            join = join.replaceAll("\\{"+i+"}", s[i]);
        }
        return formatForList(join);
    }

    public static List<String> formatForList(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        return Arrays.asList(s.split("\n"));
    }
    public static String formatForString(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
