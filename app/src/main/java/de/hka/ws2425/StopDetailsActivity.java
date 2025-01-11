package de.hka.ws2425;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.hka.ws2425.utils.CalendarDates;
import de.hka.ws2425.utils.Departure;
import de.hka.ws2425.utils.DepartureAdapter;
import de.hka.ws2425.utils.Routes;
import de.hka.ws2425.utils.Stop;
import de.hka.ws2425.utils.StopTimes;
import de.hka.ws2425.utils.Trips;

public class StopDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stop_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//GPT Start
        String stopName = getIntent().getStringExtra("STOP_NAME");
        String stopId = getIntent().getStringExtra("STOP_ID");
        setTitle(stopName != null ? stopName : "Details");

        // Lade die Daten aus dem Asset-Ordner
        try {
            List<Routes> routes = loadRoutes("routes.txt"); // Datei im Asset-Ordner
            List<Trips> trips = loadTrips("trips.txt");
            List<StopTimes> stopTimes = loadStopTimes("stop_times.txt");

        List<Departure> departures = getDeparturesForStop(stopId, stopTimes, trips, routes);

        RecyclerView recyclerView = findViewById(R.id.departureList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DepartureAdapter(departures));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //GPT Ende

        // Setze den Haltestellennamen als Titel der Activity
        if (stopName != null) {
            setTitle(stopName);
        } else {
            setTitle("Details");
        }



    }
    // Lädt die Datei routes.txt und erstellt eine Liste von Routes
    private List<Routes> loadRoutes(String fileName) throws IOException {
        List<Routes> routes = new ArrayList<>();
        AssetManager assetManager = getAssets(); // AssetManager initialisieren

        try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
            String line;
            br.readLine(); // Kopfzeile überspringen
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Werte durch Kommas trennen
                Routes route = new Routes(
                        parts[0], // agency_id
                        parts[1], // route_id
                        parts[2], // route_short_name
                        parts[3], // route_long_name
                        Integer.parseInt(parts[4]), // route_type
                        parts[5], // route_color
                        parts[6]  // route_text_color
                );
                routes.add(route);
            }
        }
        return routes;
    }

    // Lädt die Datei trips.txt und erstellt eine Liste von Trips
    //route_id,trip_id,service_id,trip_short_name,trip_headsign,direction_id,peak_offpeak,boarding_type
    private List<Trips> loadTrips(String fileName) throws IOException {
        List<Trips> trips = new ArrayList<>();
        AssetManager assetManager = getAssets();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))){
                String line;
                br.readLine(); // Kopfzeile überspringen
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(","); // Werte durch Kommas trennen
                    Trips trip = new Trips(
                            parts[0], // route_id
                            parts[1], // trip_id
                            parts[2], // service_id
                            parts[3], // trip_short_name
                            parts[4], // trip_headsign
                            Integer.parseInt(parts[5]), // peak_offpeak
                            Integer.parseInt(parts[6]) // boarding_type
                    );
                    trips.add(trip);
                }
            }
        return trips;
    }

    // Lädt die Datei stop_times.txt und erstellt eine Liste von Stopzeiten
    //trip_id,stop_id,arrival_time,departure_time,stop_sequence,pickup_type,departure_buffer
    private List<StopTimes> loadStopTimes(String fileName) throws IOException {
        List<StopTimes> stopTimes = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))){
            String line;
            br.readLine(); // Kopfzeile überspringen
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Werte durch Kommas trennen
                StopTimes stopTime = new StopTimes(
                        parts[0], // trip_id
                        parts[1], // stop_id
                        parts[2], // arrival_time
                        parts[3], // departure_time
                        Integer.parseInt(parts[4]), // stop_sequence
                        Integer.parseInt(parts[5]), // pickup_type
                        Integer.parseInt(parts[6]) // departure_buffer
                );
                stopTimes.add(stopTime);
            }
        }
        return stopTimes;
    }

    // Lädt die Datei calendar_dates.txt und erstellt eine Liste von Kalenderdaten
    //service_id,date,exception_type
    private List<CalendarDates> loadCalendaDates(File calendarDatesFile) throws IOException {
        List<CalendarDates> calendarDates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(calendarDatesFile))){
            String line;
            br.readLine(); // Kopfzeile überspringen
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Werte durch Kommas trennen
                CalendarDates calendarDate = new CalendarDates(
                        parts[0], // trip_id
                        parts[1], // stop_id
                        Integer.parseInt(parts[2]) // departure_buffer
                );
                calendarDates.add(calendarDate);
            }
        }
        return calendarDates;
    }

    private List<Departure> getDeparturesForStop(String stopId, List<StopTimes> stopTimes,
                                                 List<Trips> trips, List<Routes> routes) {
        List<Departure> departures = new ArrayList<>();

        // 1. Filtere alle StopTimes für die gegebene stop_id
        List<StopTimes> stopTimesForStop = new ArrayList<>();
        for (StopTimes stopTime : stopTimes) {
            if (stopTime.getStop_id().equals(stopId)) {
                stopTimesForStop.add(stopTime);
            }
        }

        // 2. Verknüpfe StopTimes mit Trips und Routes
        for (StopTimes stopTime : stopTimesForStop) {
            // Hole den Trip für die Trip-ID
            Trips trip = null;
            for (Trips t : trips) {
                if (t.getTrip_id().equals(stopTime.getTrip_id())) {
                    trip = t;
                    break;
                }
            }

            if (trip != null) {
                // Hole die Route für die Route-ID des Trips
                Routes route = null;
                for (Routes r : routes) {
                    if (r.getRoute_id().equals(trip.getRoute_id())) {
                        route = r;
                        break;
                    }
                }

                if (route != null) {
                    // Erstelle ein Departure-Objekt
                    departures.add(new Departure(
                            route.getRoute_short_name(), // Kurzname der Route
                            trip.getTrip_headsign(),     // Zielort
                            stopTime.getArrival_time(),  // Ankunftszeit
                            stopTime.getDeparture_time() // Abfahrtszeit
                    ));
                }
            }
        }

        // 3. Sortiere die Abfahrten nach Abfahrtszeit
        departures.sort(Comparator.comparing(Departure::getDepartureTime));

        return departures;
    }


}