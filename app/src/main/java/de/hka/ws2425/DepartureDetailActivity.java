package de.hka.ws2425;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hka.ws2425.utils.Adapter.TripDetailAdapter;
import de.hka.ws2425.utils.TripStop;

public class DepartureDetailActivity extends AppCompatActivity {

    private RecyclerView departureRecyclerView;
    private TripDetailAdapter tripDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_details);

        Intent intent = getIntent();
        String routeShortName = intent.getStringExtra("ROUTE_SHORT_NAME");
        String tripHeadsign = intent.getStringExtra("TRIP_HEADSIGN");
        String arrivalTime = intent.getStringExtra("ARRIVAL_TIME");
        String departureTime = intent.getStringExtra("DEPARTURE_TIME");
        String tripID = intent.getStringExtra("TRIP_ID");


        // UI-Elemente befüllen
        TextView routeNameTextView = findViewById(R.id.routeShortNameTextView);
        TextView tripHeadsignTextView = findViewById(R.id.routeDesinationNameTextView);

        if (routeNameTextView != null) routeNameTextView.setText(routeShortName);
        if (tripHeadsignTextView != null) tripHeadsignTextView.setText(tripHeadsign);

        // RecyclerView initialisieren
        departureRecyclerView = findViewById(R.id.departureList);
        departureRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Beispielhafte Liste der Trip-Daten erstellen
        List<TripStop> tripStops = fetchTripStops(tripID);

        // Adapter setzen
        tripDetailAdapter = new TripDetailAdapter(tripStops);
        departureRecyclerView.setAdapter(tripDetailAdapter);
    }


    private List<TripStop> fetchTripStops(String tripID) {
        // In einer echten Anwendung würden hier die Daten aus einer Datenbank oder API abgerufen werden.
        // Beispiel-Daten:
        List<TripStop> tripStops = new ArrayList<>();
        tripStops.add(new TripStop("08:30", "Uhingen Schulstraße"));
        tripStops.add(new TripStop("08:45", "Göppingen Bahnhof"));
        tripStops.add(new TripStop("09:00", "Eislingen Markt"));
        return tripStops;
    }

}
