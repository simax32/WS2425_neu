package de.hka.ws2425.utils;

public class Trips {

    private String route_id;
    private String trip_id;
    private String service_id;
    private String trip_short_name;
    private String trip_headsign;
    private int direction_id;
    private int peak_offpeak;

    public Trips(String routeId, String tripId, String serviceId, String tripShortName, String tripHeadsign, int directionId, int peakOffpeak) {
        this.route_id = routeId;
        this.trip_id = tripId;
        this.service_id = serviceId;
        this.trip_short_name = tripShortName;
        this.trip_headsign = tripHeadsign;
        this.direction_id = directionId;
        this.peak_offpeak = peakOffpeak;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getTrip_short_name() {
        return trip_short_name;
    }

    public void setTrip_short_name(String trip_short_name) {
        this.trip_short_name = trip_short_name;
    }

    public String getTrip_headsign() {
        return trip_headsign;
    }

    public void setTrip_headsign(String trip_headsign) {
        this.trip_headsign = trip_headsign;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(int direction_id) {
        this.direction_id = direction_id;
    }

    public int getPeak_offpeak() {
        return peak_offpeak;
    }

    public void setPeak_offpeak(int peak_offpeak) {
        this.peak_offpeak = peak_offpeak;
    }
}