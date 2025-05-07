package com.flightsearch.flight_app.model;

public class Flight {
    private int id;
    private String departure;
    private String destination;
    private String departureDate;
    private String returnDate;
    private int passengers;
    private double price;

    // Constructor
    public Flight(int id, String departure, String destination, String departureDate, String returnDate, int passengers, double price) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.passengers = passengers;
        this.price = price;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}