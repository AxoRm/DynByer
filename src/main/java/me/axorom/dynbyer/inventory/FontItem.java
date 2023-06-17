package me.axorom.dynbyer.inventory;

import java.util.List;

public class FontItem {
    private final String material;
    private final String name;
    private final String lore;
    private final List<Integer> slots;

    public FontItem(String material, String name, String lore, List<Integer> slots) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slots = slots;
    }

    public String getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public List<Integer> getSlots() {
        return slots;
    }
}
