package com.example.hetavdesai.pl2project;

import java.math.BigInteger;

public class UserClass {

    private String name;
    private String number;
    private String email;
    private String userid;
    private String group;
    private int tableno;

    public UserClass() {
    }

    public UserClass(String name, String number, String email, String group, int tableno) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.group = group;
        this.tableno = tableno;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getTableno() {
        return tableno;
    }

    public void setTableno(int tableno) {
        this.tableno = tableno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
