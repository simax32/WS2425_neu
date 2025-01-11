package de.hka.ws2425.utils;

public class Departure {
    private String routeShortName; // Kurzname der Route
    private String tripHeadsign;   // Zielort
    private String arrivalTime;    // Ankunftszeit
    private String departureTime;  // Abfahrtszeit

    public Departure(String routeShortName, String tripHeadsign, String arrivalTime, String departureTime) {
        this.routeShortName = routeShortName;
        this.tripHeadsign = tripHeadsign;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    // Getter und Setter
    public String getRouteShortName() { return routeShortName; }
    public String getTripHeadsign() { return tripHeadsign; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDepartureTime() { return departureTime; }
}
