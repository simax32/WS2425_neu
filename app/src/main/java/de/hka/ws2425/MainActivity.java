package de.hka.ws2425;


import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.gtfs.reader.*; //Import des GTFS Reader
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import de.hka.ws2425.ui.main.MainFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();

        }

        File zippy = new File(getApplicationContext().getCacheDir(), "/gtfs-hka-j25.zip");

        if (!zippy.exists()) {
            copyAsset(getAssets(), zippy, "gtfs-hka-j25.zip");
            // gtfs wird vom Assetordner kopiert in ein cache(wo das file darauf zeigt)
        }

        GtfsSimpleDao gtfsDao = new GtfsSimpleDao();        //erstellen eines Objektes gtfsDao

        GtfsReader gtfsReader = new GtfsReader();           //erstellen eines Objektes gtfsReader
        gtfsReader.setDataAccessObject(gtfsDao);
        try {
            gtfsReader.read(zippy.getAbsolutePath());
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Error loading file: " + e.getMessage());
            e.printStackTrace();
        }

        gtfsDao.getStops().forEach(stop -> {                                //Holt Name von Stationen aus gtfs
            Log.d(this.getClass().getSimpleName(), stop.getName());
        });
        gtfsDao.getAgencies().forEach(agency -> {
            Log.d(this.getClass().getSimpleName(), agency.getName());       //Holt Name von Bürgerbussen aus gtfs
        });

    }
    //Start Code zum Auslesen und Schreiben von gtfs
    private static boolean copyAsset(AssetManager am, File file, String fileName) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = am.open(fileName);                     //öffnet die gtfs file
            out = new FileOutputStream(file);           //schreibt in Datei die Informationen aus gtfs
            byte[] buffer = new byte[1024];             //größe, wie viel ausgelesen wird in bytes
            int length;                                 //length sagt, wie viel tatsächlich ausgelesen wird (kann auch weniger als 1024 bytes sein)
            while ((length = in.read(buffer)) > 0) {    //versucht den buffer zu füllen
                out.write(buffer, 0, length);       //buffer wird in file geschrieben
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}