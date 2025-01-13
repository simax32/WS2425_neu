package de.hka.ws2425;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import de.hka.ws2425.ui.map.MapFragment;
import de.hka.ws2425.utils.Adapter.TripDetailAdapter;
import de.hka.ws2425.utils.Stop;
import de.hka.ws2425.utils.StopTimes;
import de.hka.ws2425.utils.TripStop;

public class DepartureDetailActivity extends AppCompatActivity {

    private RecyclerView departureRecyclerView;
    private TripDetailAdapter tripDetailAdapter;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_details);


        // Daten aus Intent holen
        Intent intent = getIntent();
        String tripID = intent.getStringExtra("TRIP_ID");
        String routeShortName = intent.getStringExtra("ROUTE_SHORT_NAME");
        String tripHeadsign = intent.getStringExtra("TRIP_HEADSIGN");

        // Debugging: Überprüfe empfangene Daten
        Log.d("DepartureDetailActivity", "Erhaltene Daten: TripID = " + tripID +
                ", RouteShortName = " + routeShortName + ", TripHeadsign = " + tripHeadsign);


        setTitle("Linieienverlauf " +routeShortName +" " + tripHeadsign);


        // RecyclerView initialisieren
        departureRecyclerView = findViewById(R.id.departureList);
        departureRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Schließt die aktuelle Activity und kehrt zur vorherigen zurück
                finish();
            }
        });

        FloatingActionButton fabMapBack = findViewById(R.id.fabMapBack);
        fabMapBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Schließt die aktuelle Activity und kehrt zur vorherigen zurück
                Intent intent = new Intent();
                intent.putExtra("returnMap", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        RecyclerView departureList = findViewById(R.id.departureList);
        FloatingActionButton fabMap = findViewById(R.id.fabMapBack);



        // Daten laden
        List<Stop> stops;
        List<StopTimes> stopTimes;

        try {
            stops = loadStops("stops.txt");
            stopTimes = loadStopTimes("stop_times.txt");
        } catch (IOException e) {
            Log.e("DepartureDetailActivity", "Fehler beim Laden der Dateien: " + e.getMessage(), e);
            Toast.makeText(this, "Fehler beim Laden der Daten.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Liste der TripStops erstellen
        List<TripStop> tripStops = fetchTripStops(tripID, stopTimes, stops);

        // Adapter setzen
        tripDetailAdapter = new TripDetailAdapter(tripStops);
        departureRecyclerView.setAdapter(tripDetailAdapter);

        // Debugging: Überprüfe Liste
        Log.d("DepartureDetailActivity", "RecyclerView mit TripStops initialisiert: " + tripStops.size());
    }

    /**
     * Diese Methode lädt Stops aus der Datei "stops.txt".
     */
    private List<Stop> loadStops(String fileName) throws IOException {
        List<Stop> stops = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
            reader.readLine(); // Kopfzeile überspringen
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    stops.add(new Stop(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
                } else {
                    Log.w("loadStops", "Ungültige Zeile: " + line);
                }
            }
        }
        Log.d("loadStops", "Geladene Stops: " + stops.size());
        return stops;
    }

    /**
     * Diese Methode lädt StopTimes aus der Datei "stop_times.txt".
     */
    private List<StopTimes> loadStopTimes(String fileName) throws IOException {
        List<StopTimes> stopTimes = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
            reader.readLine(); // Kopfzeile überspringen
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    stopTimes.add(new StopTimes(parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5], Integer.parseInt(parts[6])));
                } else {
                    Log.w("loadStopTimes", "Ungültige Zeile: " + line);
                }
            }
        }
        Log.d("loadStopTimes", "Geladene StopTimes: " + stopTimes.size());
        return stopTimes;
    }

    /**
     * Diese Methode filtert StopTimes für die gegebene TripID und mappt sie zu einer Liste von TripStops.
     */
    private List<TripStop> fetchTripStops(String tripID, List<StopTimes> stopTimes, List<Stop> stops) {
        List<TripStop> tripStops = new ArrayList<>();

        // StopTimes für die gegebene TripID filtern
        List<StopTimes> filteredStopTimes = stopTimes.stream()
                .filter(stopTime -> tripID.equals(stopTime.getTrip_id()))
                .sorted(Comparator.comparing(StopTimes::getDeparture_time))
                .collect(Collectors.toList());

        Log.d("fetchTripStops", "Gefilterte StopTimes: " + filteredStopTimes.size());

        // Zu jedem StopTime den zugehörigen Stop finden
        for (StopTimes stopTime : filteredStopTimes) {
            Stop stop = stops.stream()
                    .filter(s -> s.getStop_id().equals(stopTime.getStop_id()))
                    .findFirst()
                    .orElse(null);

            if (stop != null) {
                tripStops.add(new TripStop(stopTime.getDeparture_time(), stop.getStop_name(), stopTime.getArrival_time()));
                Log.d("fetchTripStops", "TripStop hinzugefügt: StopName = " + stop.getStop_name() +
                        ", DepartureTime = " + stopTime.getDeparture_time());
            } else {
                Log.w("fetchTripStops", "Kein Stop gefunden für StopID: " + stopTime.getStop_id());
            }
        }

        return tripStops;
    }

    private String formatTime(String time) {
        try {
            Date date = inputFormat.parse(time); // Eingabezeit parsen
            return outputFormat.format(date);   // Ausgabezeit formatieren
        } catch (ParseException e) {
            e.printStackTrace();
            return time; // Falls ein Fehler auftritt, die Originalzeit zurückgeben
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void launchMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Create a new instance of MapFragment (if needed)
        MapFragment mapFragment = new MapFragment();

        transaction.replace(R.id.container, mapFragment); // Replace container with MapFragment
        transaction.addToBackStack(null); // Add to backstack (optional: provide a name)
        transaction.commit();
    }
}


