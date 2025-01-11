package de.hka.ws2425;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import de.hka.ws2425.utils.CalendarDates;
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
// Hole den Haltestellennamen aus dem Intent
        String stopName = getIntent().getStringExtra("STOP_NAME");

        // Setze den Haltestellennamen als Titel der Activity
        if (stopName != null) {
            setTitle(stopName);
        } else {
            setTitle("Details");
        }

    }
    // Lädt die Datei routes.txt und erstellt eine Liste von Routes
    private List<Routes> loadRoutes(File routesFile) throws IOException {
        List<Routes> routes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(routesFile))) {
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
    private List<Trips> loadTrips(File tripsFile) throws IOException {
        List<Trips> trips = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(tripsFile))){
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
    private List<StopTimes> loadStopTimes(File stopTimesFile) throws IOException {
        List<StopTimes> stopTimes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(stopTimesFile))){
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
}