package de.hka.ws2425.utils;

public class TripStop {
    private String departureTime;
    private String stopName;

    public TripStop(String departureTime, String stopName) {
        this.departureTime = departureTime;
        this.stopName = stopName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getStopName() {
        return stopName;
    }
}