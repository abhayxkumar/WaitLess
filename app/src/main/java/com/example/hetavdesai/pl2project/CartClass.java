package com.example.hetavdesai.pl2project;

public class CartClass {
    private String name, orderid, username;
    private int price, quantity, total, tableno;
    static int gtotal;

    CartClass() {
    }

    public CartClass(String name, int price, int quantity, int total, int tableno, String orderid, String username, int gtotal) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.tableno = tableno;
        this.orderid = orderid;
        this.username = username;
        this.gtotal = gtotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrderid() {
        return orderid;
    }

    public int getGtotal() {
        return gtotal;
    }

    public void setGtotal(int gtotal) {
        this.gtotal = gtotal;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTableno() {
        return tableno;
    }

    public void setTableno(int tableno) {
        this.tableno = tableno;
    }
}


