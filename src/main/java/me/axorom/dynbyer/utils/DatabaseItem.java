package me.axorom.dynbyer.utils;

public class DatabaseItem {
    double coefficient;
    int blockLeft;

    public double getCoefficient() {
        return coefficient;
    }

    public int getBlockLeft() {
        return blockLeft;
    }

    public String getMaterial() {
        return material;
    }

    String material;

    public DatabaseItem(double coefficient, int blockLeft, String material) {
        this.coefficient = coefficient;
        this.blockLeft = blockLeft;
        this.material = material;
    }
}
