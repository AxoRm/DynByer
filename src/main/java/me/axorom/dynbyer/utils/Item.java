package me.axorom.dynbyer.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private final String id;
    private final int startPrice;
    private final double coefficient;

    private final int period;
    private final int slot;

    public Item(String id, int startPrice, double coefficient, int period, int slot) {
        this.id = id;
        this.startPrice = startPrice;
        this.coefficient = coefficient;
        this.period = period;
        this.slot = slot;
    }

    public String getId() {
        return id;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public int getPeriod() {
        return period;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public int getSlot() {
        return slot;
    }
}
