package com.example.hetavdesai.pl2project;

public class SplitClass {
    private String itemName, itemPrice, itemQuantity;
    static int personalTotal;

    SplitClass() {
    }

    public SplitClass(String itemName, String itemPrice, String itemQuantity, int personalTotal) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.personalTotal = personalTotal;
    }

    public static int getPersonalTotal() {
        return personalTotal;
    }

    public static void setPersonalTotal(int personalTotal) {
        SplitClass.personalTotal = personalTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
