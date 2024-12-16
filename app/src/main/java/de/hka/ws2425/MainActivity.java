package de.hka.ws2425;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import org.gtfs.reader.*;

import java.util.ArrayList;
import java.util.List;

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
    }
}