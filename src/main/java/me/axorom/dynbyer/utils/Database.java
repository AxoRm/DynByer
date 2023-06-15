package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    YamlConfiguration db;
    public static Map<String, Map<String, DatabaseItem>> databaseItems;
    DynByer plugin = DynByer.instance;

    public Database() {
        reloadBase();
    }

    public void reloadBase() {
        File configFile = new File(plugin.getDataFolder(), "db.yml");
        if (!configFile.exists()) plugin.saveResource("db.yml", false);
        db = YamlConfiguration.loadConfiguration(configFile);
        databaseItems = new HashMap<>();
        db.getKeys(false).forEach(player -> {
            ConfigurationSection section = db.getConfigurationSection(player);
            Map<String, DatabaseItem> list = new HashMap<>();
            section.getKeys(false).forEach(item -> {
                list.put(item, new DatabaseItem(section.getInt(item), item));
            });
            databaseItems.put(player, list);
        });
    }

    public void save() {
        databaseItems.forEach((k, vList) -> {
            vList.forEach((ignored, v) -> {
                db.set(k+"."+v.getMaterial()+".selled", v.getSelled());
            });
        });

        try {
            db.save(new File(plugin.getDataFolder(), "db.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to save config file: " + e.getMessage());
        }
    }
}