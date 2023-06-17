package me.axorom.dynbyer.inventory;

import java.util.ArrayList;

public class FontItem {
    private String material;
    private String name;
    private String lore;
    private ArrayList<Integer> slots;

    public FontItem(String material, String name, String lore, ArrayList<Integer> slots) {
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

    public ArrayList<Integer> getSlots() {
        return slots;
    }
}
