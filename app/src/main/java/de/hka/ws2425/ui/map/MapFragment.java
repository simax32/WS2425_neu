package de.hka.ws2425.ui.map;

import static java.security.AccessController.getContext;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
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
import android.widget.TextView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.hka.ws2425.R;
import de.hka.ws2425.utils.Stops;


/*
ChatGPT START
private InputStream extractStopsFileFromZip(String zipFileName, String targetFileName) throws IOException {
    AssetManager assetManager = getContext().getAssets();
    InputStream zipInputStream = assetManager.open(zipFileName);
    ZipInputStream zis = new ZipInputStream(zipInputStream);

    ZipEntry entry;
    while ((entry = zis.getNextEntry()) != null) {
        if (entry.getName().equals(targetFileName)) {
            return zis; // Gibt den InputStream der Datei `stops.txt` zurück
        }
    }
    zis.close();
    throw new IOException("File " + targetFileName + " not found in " + zipFileName);
}

ChatGPT ENDE
*/

public class MapFragment extends Fragment {

    private MapViewModel mViewModel;

    private MapView mapView;

    private List<Stops> stopsList = new ArrayList<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }

    // Hinzufügen des Markers
    public void addStopMarker() {
        for (Stops stops : stopsList) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(stops.getLatitude(), stops.getLongitude())); // Position setzen
            //marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Ankerpunkt setzen (Was macht das?)
            marker.setTitle(stops.getName()); // Titel für den Marker
            mapView.getOverlays().add(marker); // Marker zur Karte hinzufügen
        }
        mapView.invalidate();
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


            return root;

    }

    @Override
    public void onResume() {
        super.onResume();

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