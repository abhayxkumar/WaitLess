package com.example.hetavdesai.pl2project;

public class OrderItemClass {
    private String itemName, itemPrice, itemQuantity, tableNo, userName;
    static int personalTotal;

    OrderItemClass() {
    }

    public OrderItemClass(String itemName, String itemPrice, String itemQuantity, String tableNo, int pTotal, String userName) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.tableNo = tableNo;
        this.personalTotal = pTotal;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPersonalTotal() {
        return personalTotal;
    }

    public void setPersonalTotal(int personalTotal) {
        this.personalTotal = personalTotal;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
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
