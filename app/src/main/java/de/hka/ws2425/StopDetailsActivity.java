package de.hka.ws2425;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.time.LocalTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hka.ws2425.utils.CalendarDates;
import de.hka.ws2425.utils.Departure;
import de.hka.ws2425.utils.DepartureAdapter;
import de.hka.ws2425.utils.Routes;
import de.hka.ws2425.utils.Stop;
import de.hka.ws2425.utils.StopTimes;
import de.hka.ws2425.utils.Trips;

public class StopDetailsActivity extends AppCompatActivity{

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

        // Logging für Debugging
        Log.d("DEBUG", "StopName: " + stopName);
        Log.d("DEBUG", "StopId: " + stopId);

        // Absicherung gegen fehlende Daten
        if (stopId == null || stopName == null) {
            Log.e("DEBUG", "Fehlende Intent-Extras! Beende die Activity.");
            Toast.makeText(this, "Fehler: Haltestelleninformationen fehlen.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Titel setzen
        setTitle(stopName);

        // Daten laden und RecyclerView initialisieren
        try {
            List<Routes> routes = loadRoutes("routes.txt");
            List<Trips> trips = loadTrips("trips.txt");
            List<StopTimes> stopTimes = loadStopTimes("stop_times.txt");

            List<Departure> departures = getDeparturesForStop(stopId, stopTimes, trips, routes);

            RecyclerView recyclerView = findViewById(R.id.departureList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            departures = getDeparturesForStop(stopId, stopTimes, trips, routes); // Assign the list
            recyclerView.setAdapter(new DepartureAdapter(departures));

            Log.d("DEBUG", "Adapter erfolgreich an RecyclerView gebunden mit " + departures.size() + " Einträgen.");
        } catch (IOException e) {
            Log.e("DEBUG", "Fehler beim Laden der Daten: " + e.getMessage(), e);
            Toast.makeText(this, "Fehler beim Laden der Abfahrten.", Toast.LENGTH_SHORT).show();
        }

    }


//Ende

    // Lädt die Datei routes.txt und erstellt eine Liste von Routes
    private List<Routes> loadRoutes(String fileName) throws IOException {
        List<Routes> routes = new ArrayList<>();
        AssetManager assetManager = getAssets(); // AssetManager initialisieren

        Log.d("DEBUG", "Versuche Datei zu laden: " + fileName);
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
        catch (Exception e) {
            Log.e("DEBUG", "Fehler beim Laden der Datei: " + fileName, e);
        }
        Log.d("DEBUG", "Insgesamt geladene Routen: " + routes.size());
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
            catch (Exception e) {
                Log.e("DEBUG", "Fehler beim Laden der Datei: " + fileName, e);
            }
        Log.d("DEBUG", "Insgesamt geladene Trips: " + trips.size());
        return trips;
    }

    // Lädt die Datei stop_times.txt und erstellt eine Liste von Stopzeiten
    //trip_id,stop_id,arrival_time,departure_time,stop_sequence,pickup_type,departure_buffer

    private List<StopTimes> loadStopTimes(String fileName) throws IOException {
        List<StopTimes> stopTimes = new ArrayList<>();
        AssetManager assetManager = getAssets();

        Log.d("DEBUG", "Beginne mit dem Laden der Datei: " + fileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
            String line;
            br.readLine(); // Kopfzeile überspringen
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                Log.d("DEBUG", "Lese Zeile " + lineNumber + ": " + line);

                String[] parts = line.split(",");
                if (parts.length < 7) {
                    Log.e("DEBUG", "Zeile hat weniger als 7 Spalten in Zeile " + lineNumber + ": " + line);
                    continue;
                }

                try {
                    // Entferne Leerzeichen und überprüfe Daten
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim(); // Entfernt unnötige Leerzeichen
                    }

                    // Prüfen, ob wichtige Felder vorhanden sind
                    if (parts[1].isEmpty() || parts[0].isEmpty() || parts[2].isEmpty() || parts[3].isEmpty()) {
                        Log.e("DEBUG", "Wichtige Daten fehlen in Zeile " + lineNumber + ": " + line);
                        continue;
                    }

                    // Erstelle und füge StopTime-Objekt hinzu
                    StopTimes stopTime = new StopTimes(
                            parts[0], // trip_id
                            parts[1], // stop_id
                            parts[2], // arrival_time
                            parts[3], // departure_time
                            Integer.parseInt(parts[4]), // stop_sequence
                            parts[5], // pickup_type
                            Integer.parseInt(parts[6])  // departure_buffer
                    );
                    stopTimes.add(stopTime);

                    Log.d("DEBUG", "Geladene StopTime: " + stopTime.toString());
                } catch (NumberFormatException e) {
                    Log.e("DEBUG", "Fehler beim Konvertieren von Zahlen in Zeile " + lineNumber + ": " + line, e);
                } catch (Exception e) {
                    Log.e("DEBUG", "Unbekannter Fehler in Zeile " + lineNumber + ": " + line, e);
                }
            }
        } catch (IOException e) {
            Log.e("DEBUG", "Fehler beim Öffnen der Datei: " + fileName, e);
            throw e; // Fehler erneut werfen, um darauf zu reagieren
        }

        Log.d("DEBUG", "Insgesamt geladene Stopzeiten: " + stopTimes.size());
        return stopTimes;
    }





    // Lädt die Datei calendar_dates.txt und erstellt eine Liste von Kalenderdaten
    //service_id,date,exception_type
    private List<CalendarDates> loadCalendaDates(String fileName) throws IOException {
        List<CalendarDates> calendarDates = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))){
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
        Log.d("DEBUG", "Starte Filterung der Abfahrten für Stop-ID: " + stopId);

        if (stopId == null || stopId.isEmpty()) {
            Log.e("DEBUG", "Stop-ID ist null oder leer. Rückgabe einer leeren Abfahrtsliste.");
            return departures;
        }

        // 1. Mapping für schnellere Zuordnung
        Map<String, Trips> tripsMap = new HashMap<>();
        for (Trips trip : trips) {
            if (trip.getTrip_id() != null) {
                tripsMap.put(trip.getTrip_id(), trip);
            }
        }

        Map<String, Routes> routesMap = new HashMap<>();
        for (Routes route : routes) {
            if (route.getRoute_id() != null) {
                routesMap.put(route.getRoute_id(), route);
            }
        }

        // 2. Filtere die StopTimes für die gegebene stop_id
        for (StopTimes stopTime : stopTimes) {
            if (stopTime == null || stopTime.getStop_id() == null || stopTime.getTrip_id() == null) {
                Log.e("DEBUG", "Fehlerhafte StopTime-Einträge übersprungen: " + stopTime);
                continue;
            }

            if (!stopTime.getStop_id().equalsIgnoreCase(stopId)) {
                continue;
            }

            // 3. Hole den Trip für die Trip-ID
            Trips trip = tripsMap.get(stopTime.getTrip_id());
            if (trip == null) {
                Log.e("DEBUG", "Kein Trip gefunden für Trip-ID: " + stopTime.getTrip_id());
                continue;
            }

            // 4. Hole die Route für die Route-ID des Trips
            Routes route = routesMap.get(trip.getRoute_id());
            if (route == null) {
                Log.e("DEBUG", "Keine Route gefunden für Route-ID: " + trip.getRoute_id());
                continue;
            }

            // 5. Departure erstellen
            Departure departure = new Departure(
                    route.getRoute_short_name(), // Kurzname der Route
                    trip.getTrip_headsign(),     // Zielort
                    stopTime.getArrival_time(),  // Ankunftszeit
                    stopTime.getDeparture_time() // Abfahrtszeit
            );
            departures.add(departure);

            Log.d("DEBUG", "Departure hinzugefügt: " + departure.getRouteShortName() + ", " +
                    departure.getTripHeadsign() + ", " +
                    departure.getArrivalTime() + " - " +
                    departure.getDepartureTime());
        }

        // 6. Sortiere die Abfahrten nach Abfahrtszeit
        departures.sort(Comparator.comparing(Departure::getDepartureTime));

        Log.d("DEBUG", "Insgesamt gefundene Abfahrten: " + departures.size());
        return departures;
    }

}