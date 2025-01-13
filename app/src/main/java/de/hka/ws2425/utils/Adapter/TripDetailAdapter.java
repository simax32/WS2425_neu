package de.hka.ws2425.utils.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hka.ws2425.R;
import de.hka.ws2425.utils.TripStop;

public class TripDetailAdapter extends RecyclerView.Adapter<TripDetailAdapter.TripDetailViewHolder> {

    private final List<TripStop> tripStops;

    public TripDetailAdapter(List<TripStop> tripStops) {
        this.tripStops = tripStops;
    }

    @NonNull
    @Override
    public TripDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_departure_detail, parent, false);
        return new TripDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDetailViewHolder holder, int position) {
        TripStop tripStop = tripStops.get(position);
        holder.tripStopDepartureTime.setText(formatTime(tripStop.getDepartureTime()) + " Uhr");
        holder.tripStopShortName.setText(tripStop.getStopName());
    }

    @Override
    public int getItemCount() {
        return tripStops.size();
    }

    static class TripDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tripStopDepartureTime, tripStopShortName;

        public TripDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tripStopDepartureTime = itemView.findViewById(R.id.tripStopDepartureTime);
            tripStopShortName = itemView.findViewById(R.id.tripStopShortName);
        }
    }
    private String formatTime(String time) {
        try {
            if (time == null) return "";
            String[] parts = time.split(":");
            if (parts.length >= 2) {
                return parts[0] + ":" + parts[1]; // Nur Stunden und Minuten übernehmen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time; // Bei Fehlern das Original zurückgeben
    }
}

