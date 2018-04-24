package com.example.arvind.hackathon;

public class Blog1 {

    private String boarding;
    private String destination;
    private String bill;
    private String balance;

    public Blog1() {

    }

    public Blog1(String boarding, String destination, String bill, String balance) {
        this.boarding = boarding;
        this.destination = destination;
        this.bill = bill;
        this.balance = balance;
    }

    public String getBoarding() {
        return boarding;
    }

    public void setBoarding(String boarding) {
        this.boarding = boarding;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}



