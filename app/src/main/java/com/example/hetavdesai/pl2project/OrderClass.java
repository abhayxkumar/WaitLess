package com.example.hetavdesai.pl2project;

public class OrderClass {
    private String orderId, orderTime;
    private int orderTotal, tableNo;

    OrderClass() {
    }

    public OrderClass(int tableNo, String orderId, String orderTime, int orderTotal) {
        this.tableNo = tableNo;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderTotal = orderTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }
}