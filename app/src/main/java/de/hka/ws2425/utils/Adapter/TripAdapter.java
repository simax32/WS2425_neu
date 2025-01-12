package de.hka.ws2425.utils.Adapter;

import org.gtfs.reader.model.Trip;

import java.util.List;

import de.hka.ws2425.utils.Departure;

public class TripAdapter {

    private List<Trip> trips;

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
    }


}
