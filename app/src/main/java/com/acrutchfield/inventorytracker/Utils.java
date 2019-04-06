package com.acrutchfield.inventorytracker;

public class Utils {
    public static String getDocumentId(String location, String spot, String partnumber) {
        return location + "_" + spot + "_" + partnumber;
    }
}
