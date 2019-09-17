package com.example.hetavdesai.pl2project;

public class ReservationClass {
    private String reservationId, date, numberOfPeople, time, name, email, phone, status;

    public ReservationClass(String reservationId, String date, String numberOfPeople, String time, String name, String email, String phone, String status) {
        this.reservationId = reservationId;
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.time = time;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public ReservationClass() {
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
