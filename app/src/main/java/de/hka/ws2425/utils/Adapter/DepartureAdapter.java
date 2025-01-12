package de.hka.ws2425.utils.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hka.ws2425.DepartureDetailActivity;
import de.hka.ws2425.R;
import de.hka.ws2425.utils.Departure;
import io.reactivex.annotations.NonNull;

public class DepartureAdapter extends RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder> {

    private List<Departure> departures;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

    public DepartureAdapter(List<Departure> departures) {
        this.departures = departures;
    }

    @NonNull
    @Override
    public DepartureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_departure, parent, false);
        return new DepartureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartureViewHolder holder, int position) {
        Departure departure = departures.get(position);
        holder.routeShortName.setText("Linie " + departure.getRouteShortName());
        holder.tripHeadsign.setText(departure.getTripHeadsign());

        // Formatieren der Zeiten
        holder.arrivalTime.setText("Ankunft " + formatTime(departure.getArrivalTime()) + " Uhr");
        holder.departureTime.setText(formatTime(departure.getDepartureTime()) + " Uhr");

        holder.itemView.setOnClickListener(v -> {
            // Handle item click here
            Intent intent = new Intent(v.getContext(), DepartureDetailActivity.class);

            // Pass data as extras
            intent.putExtra("ROUTE_SHORT_NAME", departure.getRouteShortName());
            intent.putExtra("TRIP_HEADSIGN", departure.getTripHeadsign());
            intent.putExtra("ARRIVAL_TIME", departure.getArrivalTime());
            intent.putExtra("DEPARTURE_TIME", departure.getDepartureTime());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return departures.size();
    }

    // Methode zum Formatieren der Uhrzeiten
    private String formatTime(String time) {
        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return time; // Falls ein Fehler auftritt, bleibt die Originalzeit
        }
    }

    public static class DepartureViewHolder extends RecyclerView.ViewHolder {
        TextView routeShortName, tripHeadsign, arrivalTime, departureTime;

        public DepartureViewHolder(@NonNull View itemView) {
            super(itemView);
            routeShortName = itemView.findViewById(R.id.routeShortName);
            tripHeadsign = itemView.findViewById(R.id.tripHeadsign);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            departureTime = itemView.findViewById(R.id.departureTime);
        }
    }
}

