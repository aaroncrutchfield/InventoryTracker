package com.acrutchfield.inventorytracker.data;

import java.util.List;

public class MetaData {
    private List<String> customers;
    private String defaultLocation;
    private int inventoryTotal;
    private String itemNumber;
    private String revLevel;
    private String standardContainer;
    private int standardPack;
    private String suffix;
    private String imageUrl;

    public MetaData(List<String> customers, String defaultLocation, int inventoryTotal, String itemNumber, String revLevel, String standardContainer, int standardPack, String suffix, String imageUrl) {
        this.customers = customers;
        this.defaultLocation = defaultLocation;
        this.inventoryTotal = inventoryTotal;
        this.itemNumber = itemNumber;
        this.revLevel = revLevel;
        this.standardContainer = standardContainer;
        this.standardPack = standardPack;
        this.suffix = suffix;
        this.imageUrl = imageUrl;
    }

    public MetaData() {

    }

    public String getCompleteItemNumber() {
        if (revLevel != null && suffix != null) {
            return itemNumber + "-" + revLevel + "-" + suffix;
        } else {
            return itemNumber;
        }
    }

    public void setItemProperties(String completeItemNumber) {
        if (completeItemNumber.contains("-")) {
            String[] properties = completeItemNumber.split("-");
            itemNumber = properties[0];
            revLevel = properties[1];
            suffix = properties[2];
        } else {
            itemNumber = completeItemNumber;
        }
    }

    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(List<String> customers) {
        this.customers = customers;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public int getInventoryTotal() {
        return inventoryTotal;
    }

    public void setInventoryTotal(int inventoryTotal) {
        this.inventoryTotal = inventoryTotal;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getRevLevel() {
        return revLevel;
    }

    public void setRevLevel(String revLevel) {
        this.revLevel = revLevel;
    }

    public String getStandardContainer() {
        return standardContainer;
    }

    public void setStandardContainer(String standardContainer) {
        this.standardContainer = standardContainer;
    }

    public int getStandardPack() {
        return standardPack;
    }

    public void setStandardPack(int standardPack) {
        this.standardPack = standardPack;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getUpdatedTotal(int quantitiyToAdd) {
        inventoryTotal += quantitiyToAdd;
        return inventoryTotal;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
