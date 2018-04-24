package com.example.arvind.hackathon;

public class Blog {

    private String name;
    private String busStop;
    private String numberOfPassengers;

    public Blog() {

    }

    public Blog(String name, String busStop, String numberOfPassengers) {
        this.name = name;
        this.busStop = busStop;
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getName() {
        return name;
    }

    public String getBusStop() {
        return busStop;
    }

    public String getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setBusStop(String busStop) {

        this.busStop = busStop;
    }
    public void setNumberOfPassengers(String numberOfPassengers) {

        this.numberOfPassengers = numberOfPassengers;
    }



}



