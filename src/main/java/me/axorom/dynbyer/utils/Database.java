package me.axorom.dynbyer.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("PlayerData")
public class Database implements ConfigurationSerializable {
    public static String playerName;
    public static HashMap<String, Integer> blockCounts;

    public Database(String playerName, HashMap<String, Integer> blockCounts) {
        this.playerName = playerName;
        this.blockCounts = blockCounts;
    }

    public static Database valueOf(Map<String, Object> serialized) {
        String playerName = (String) serialized.get("playerName");
        HashMap<String, Integer> blockCounts = (HashMap<String, Integer>) serialized.get("blockCounts");
        return new Database(playerName, blockCounts);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("playerName", playerName);
        serialized.put("blockCounts", blockCounts);
        return serialized;
    }
}