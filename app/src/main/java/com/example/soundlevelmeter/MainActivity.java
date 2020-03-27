package com.example.soundlevelmeter;

import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean doubleClick = false;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sound_meter, R.id.nav_statistics, R.id.nav_notes)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings: {
                if (Objects.requireNonNull
                        (navController.getCurrentDestination()).getId() != R.id.fragment_settings) {
                    navController.navigate(R.id.open_fragment_settings);
                }

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() ==
                R.id.nav_sound_meter) {

            if (doubleClick) {
                Log.d("EEE", "if (doubleClick) {");
                stopService(new Intent(this, MyService.class));
                Log.d("EEE", "stopService(new Intent");
                super.onBackPressed();
                Log.d("EEE", "super.onBackPressed()");

                return;
            }
            doubleClick = true;
            Toast.makeText(getApplicationContext(), R.string.double_click, Toast.LENGTH_SHORT).show();
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    doubleClick = false;

                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 2500);
        } else {
            super.onBackPressed();
        }



    }
}
