package me.axorom.dynbyer.utils;

import me.axorom.dynbyer.DynByer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MessagesParser {
    public YamlConfiguration config;
    DynByer plugin;

    public MessagesParser(DynByer plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) plugin.saveResource("messages.yml", false);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<String> getStringList(String key) {
        return config.getStringList(key).stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }

    public String format(String join, String ... s) {
        for (int i = 0; i < s.length; i++) {
            join = join.replaceAll("\\{"+i+"}", s[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', join);
    }
    public String formatPlaceholder(Player player, String join, String ... s) {
        return ChatColor.translateAlternateColorCodes( '&', PlaceholderAPI.setPlaceholders(player, format(join, s)));
    }
    public String getString(String key) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(key, ""));
    }
}