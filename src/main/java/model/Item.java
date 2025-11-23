package main.java.model;

public class Item {
    private final int id;
    private final String internalName;
    private final double priceWithVat;
    private final String displayName;

    private static final double VAT_FACTOR = 1.12;

    public Item(int id, String internalName, double priceWithVat, String displayName) {
        this.id = id;
        this.internalName = internalName;
        this.priceWithVat = priceWithVat;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }
    public String getInternalName() {
        return internalName;
    }
    public double getPriceWithVat() {
        return priceWithVat;
    }
    public String getDisplayName() {
        return displayName;
    }
    public double getBasePrice() {
        return priceWithVat / VAT_FACTOR;
    }
}
