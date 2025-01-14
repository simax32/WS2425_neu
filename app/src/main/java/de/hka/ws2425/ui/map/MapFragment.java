package de.hka.ws2425.ui.map;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hka.ws2425.R;
import de.hka.ws2425.StopDetailsActivity;
import de.hka.ws2425.utils.Stop;
import androidx.fragment.app.FragmentResultListener;

public class MapFragment extends Fragment {

    private MapViewModel mViewModel;

    private MapView mapView;

    private List<Stop> stopsList = new ArrayList<>();

    private final Map<Marker, Integer> markerClickCountMap = new HashMap<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        this.mapView = root.findViewById(R.id.mapView);

        XYTileSource mapServer = new XYTileSource("MapServer",
                8,
                20,
                256,
                ".png",
                new String[]{"https://tileserver.svprod01.app/styles/default/"}
        );

        String authorizationString = this.getMapServerAuthorizationString(
                "ws2223@hka",
                "LeevwBfDi#2027"
        );

        Configuration
                .getInstance()
                .getAdditionalHttpRequestProperties()
                .put("Authorization", authorizationString);

        this.mapView.setTileSource(mapServer);

        GeoPoint startPoint = new GeoPoint(49.0069, 8.8037); //Karlsruhe = aLongitude = 8.4037

        IMapController mapController = this.mapView.getController();
        mapController.setZoom(14.0);
        mapController.setCenter(startPoint);

        // Empfange Haltestellen-Daten von MainActivity:
        getParentFragmentManager().setFragmentResultListener("stopsData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                stopsList = (List<Stop>) result.getSerializable("stopsList");
                if (stopsList != null) {
                    addMarker();
                    Toast.makeText(requireContext(), "Klicke eine Haltestelle um Abfahrten anzeigen zu lassen", Toast.LENGTH_SHORT).show();
                }
            }
            FragmentManager fragmentManager = getParentFragmentManager();
        });

            return root;

    }

    public void addMarker() {
        for (Stop stop : stopsList) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(stop.getStop_lat(), stop.getStop_lon()));
            marker.setTitle(stop.getStop_name());

            // Setze das benutzerdefinierte Icon buspikto.png
            marker.setIcon(getResources().getDrawable(R.drawable.buspikto, null));

            // Klick-Listener für Marker
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                int clickCount = markerClickCountMap.getOrDefault(marker1, 0);
                clickCount++;
                markerClickCountMap.put(marker1, clickCount);

                if (clickCount == 2) {
                    openDetailsPage(stop); // Öffnet die neue Seite
                    markerClickCountMap.put(marker1, 0); // Klick-Zähler zurücksetzen
                } else {
                    marker1.showInfoWindow();
                    Toast.makeText(requireContext(), "Klicke die Haltestelle nochmal, um Abfahrten anzuzeigen.", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            mapView.getOverlays().add(marker);
        }
        mapView.invalidate(); // Karte aktualisieren
    }



    private void openDetailsPage(Stop stop) {
        Intent intent = new Intent(requireContext(), StopDetailsActivity.class);
        intent.putExtra("STOP_NAME", stop.getStop_name()); // Haltestellenname übergeben
        intent.putExtra("STOP_ID", stop.getStop_id()); // Stop-ID übergeben
        startActivity(intent); // Öffnet die Activity
        Log.d("Debug", "StopId = " + stop.getStop_id());
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mapView != null) {
            mapView.onResume();
            if (stopsList != null && !stopsList.isEmpty()) {
                addMarker();
            }
        }
        String[] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        Permissions.check(this.getContext(), permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                setupLocationListener();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach();
        }
    }

    @SuppressLint("MissingPermission")
    private void setupLocationListener()
    {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                GeoPoint startPoint = new GeoPoint(latitude, longitude);

                IMapController mapController = mapView.getController();
                mapController.setCenter(startPoint);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager locationManager = (LocationManager) this.getContext().getSystemService(
                Context.LOCATION_SERVICE
        );

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10,
                locationListener
        );
    }

    private String getMapServerAuthorizationString(String username, String password)
    {
        String authorizationString = String.format("%s:%s", username, password);
        return "Basic " + Base64.encodeToString(authorizationString.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }
}