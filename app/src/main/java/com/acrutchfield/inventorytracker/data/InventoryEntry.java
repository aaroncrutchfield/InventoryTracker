package com.acrutchfield.inventorytracker.data;

public class InventoryEntry {
    private String item;
    private String location;
    private String spot;
    private int total;

    public InventoryEntry(String item, String location, String spot, int total) {
        this.item = item;
        this.location = location;
        this.spot = spot;
        this.total = total;
    }

    public InventoryEntry() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int calculateTotal(int containers, int packQuantity) {
        return containers * packQuantity;
    }
}
