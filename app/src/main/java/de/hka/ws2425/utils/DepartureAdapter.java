package de.hka.ws2425.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hka.ws2425.R;
import io.reactivex.annotations.NonNull;

public class DepartureAdapter extends RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder> {

    private List<Departure> departures;

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
        holder.routeShortName.setText(departure.getRouteShortName());
        holder.tripHeadsign.setText(departure.getTripHeadsign());
        holder.arrivalTime.setText(departure.getArrivalTime());
        holder.departureTime.setText(departure.getDepartureTime());
    }

    @Override
    public int getItemCount() {
        return departures.size();
    }

    static class DepartureViewHolder extends RecyclerView.ViewHolder {
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
