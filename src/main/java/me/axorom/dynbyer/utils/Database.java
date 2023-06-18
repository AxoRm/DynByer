package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Database {
    YamlConfiguration db;
    public static Map<String, Map<String, DatabaseItem>> databaseItems;
    DynByer plugin = DynByer.instance;
    public long reloadTime;

    public int calls;

    public long frequency;

    public Database() {
        reloadBase();
    }

    public void reloadBase() {
        File configFile = new File(plugin.getDataFolder(), "db.yml");
        if (!configFile.exists()) plugin.saveResource("db.yml", false);
        db = YamlConfiguration.loadConfiguration(configFile);
        databaseItems = new HashMap<>();
        db.getKeys(false).forEach(player -> {
            if (Objects.equals(player, "player")) return;
            if (db.getConfigurationSection(player) != null) {
                ConfigurationSection section = db.getConfigurationSection(player);
                Map<String, DatabaseItem> list = new HashMap<>();
                assert section != null;
                section.getKeys(false).forEach(item -> list.put(item, new DatabaseItem(section.getInt(item), item)));
                databaseItems.put(player, list);
            }
        });
        reloadTime = db.getLong("reloadTime");
        calls = db.getInt("calls");
        frequency = db.getInt("frequency");
        if (reloadTime == 0) {
            reloadTime = System.currentTimeMillis();
        }
    }

    public void resetBase() {
        databaseItems = new HashMap<>();
        save();
    }
    public void save() {
        databaseItems.forEach((k, vList) -> vList.forEach((ignored, v) -> db.set(k+"."+v.getMaterial(), v.getSelled())));

        db.set("reloadTime", reloadTime);
        db.set("calls", calls);
        db.set("frequency", frequency);

        try {
            db.save(new File(plugin.getDataFolder(), "db.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to save config file: " + e.getMessage());
        }
    }
}