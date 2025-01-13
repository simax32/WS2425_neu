package de.hka.ws2425.utils;

public class TripStop {
    private String departureTime;
    private String stopName;
    private String arrivalTime;

    public TripStop(String departureTime, String stopName, String arrivalTime) {
        this.departureTime = departureTime;
        this.stopName = stopName;
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getStopName() {
        return stopName;
    }

    public String getArrivalTime() { return arrivalTime; }
}