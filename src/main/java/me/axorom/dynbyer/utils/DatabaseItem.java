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

    public void addSelled(int a) {
        this.selled += a;
    }

    public DatabaseItem(int selled, String material) {
        this.selled = selled;
        this.material = material;
    }
}
