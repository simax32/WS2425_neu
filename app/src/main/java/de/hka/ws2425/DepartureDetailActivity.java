package de.hka.ws2425;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DepartureDetailActivity extends AppCompatActivity {
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

        // Use the data to populate views in your layout
        TextView routeNameTextView = findViewById(R.id.routeShortNameTextView); // Example
        if(routeNameTextView != null) {
            routeNameTextView.setText(routeShortName);
        }
        TextView tripHeadsignTextview = findViewById(R.id.routeDesinationNameTextView); // Example
        if(tripHeadsignTextview != null) {
            tripHeadsignTextview.setText(tripHeadsign);
        }

        TextView tripIdTextview = findViewById(R.id.dummy); // Example
        if(tripIdTextview != null) {
            tripIdTextview.setText(tripID);
        }


        // ... set other text views
    }
}
