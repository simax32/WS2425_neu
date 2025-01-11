package de.hka.ws2425.utils;

public class StopTimes {
    private String trip_id;
    private String stop_id;
    private String arrival_time;
    private String departure_time;
    private int stop_sequence;
    private int pickup_type;
    private int departure_buffer;

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public int getPickup_type() {
        return pickup_type;
    }

    public void setPickup_type(int pickup_type) {
        this.pickup_type = pickup_type;
    }

    public int getDeparture_buffer() {
        return departure_buffer;
    }

    public void setDeparture_buffer(int departure_buffer) {
        this.departure_buffer = departure_buffer;
    }
}
