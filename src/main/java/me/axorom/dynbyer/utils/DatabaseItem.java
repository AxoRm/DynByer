package me.axorom.dynbyer.utils;

public class DatabaseItem {
    int selled; //
    public String getMaterial() {
        return material;
    }
    public int getSelled() {
        return selled;
    }

    String material;

    public DatabaseItem(int selled, String material) {
        this.selled = selled;
        this.material = material;
    }
}
