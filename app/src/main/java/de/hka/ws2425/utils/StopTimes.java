package de.hka.ws2425.utils;

public class StopTimes {

    // Felder der Klasse
    private String trip_id;
    private String stop_id;
    private String arrival_time;
    private String departure_time;
    private int stop_sequence;
    private String pickup_type;
    private int departure_buffer;

    // Vollständiger Konstruktor mit Validierung
    public StopTimes(String tripId, String stopId, String arrivalTime, String departureTime, int stopSequence, String pickupType, int departureBuffer) {
        this.trip_id = (tripId != null) ? tripId : ""; // Standardwert für Strings: ""
        this.stop_id = (stopId != null) ? stopId : "";
        this.arrival_time = (arrivalTime != null) ? arrivalTime : "";
        this.departure_time = (departureTime != null) ? departureTime : "";
        this.stop_sequence = stopSequence; // Standardwert für int ist 0, keine zusätzliche Prüfung erforderlich
        this.pickup_type = (pickupType != null) ? pickupType : "";
        this.departure_buffer = departureBuffer;
    }

    // Getter und Setter
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

    public String getPickup_type() {
        return pickup_type;
    }

    public void setPickup_type(String pickup_type) {
        this.pickup_type = pickup_type;
    }

    public int getDeparture_buffer() {
        return departure_buffer;
    }

    public void setDeparture_buffer(int departure_buffer) {
        this.departure_buffer = departure_buffer;
    }

    // Debug-Ausgabe der Objektinhalte
    @Override
    public String toString() {
        return "StopTimes{" +
                "trip_id='" + trip_id + '\'' +
                ", stop_id='" + stop_id + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", stop_sequence=" + stop_sequence +
                ", pickup_type='" + pickup_type + '\'' +
                ", departure_buffer=" + departure_buffer +
                '}';
    }
}

