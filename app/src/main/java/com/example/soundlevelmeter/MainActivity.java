package com.example.soundlevelmeter;

import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soundlevelmeter.Room.EventList;
import com.example.soundlevelmeter.Room.EventPoint;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Room.Save;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
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


        MyRoomDataBase db = Room.databaseBuilder(getApplicationContext(),
                MyRoomDataBase.class, "database-name").allowMainThreadQueries().build();

//        Save save = new Save();
//        save.saveName = "SAVE 1";
//        db.getDaoSave().addSave(save);

//        List<EventPoint> eventPointList = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            EventPoint eventPoint = new EventPoint();
//            if(i < 10){
//                eventPoint.idSave = 1;
//            } else {
//                eventPoint.idSave = 2;
//            }
//            eventPoint.time = 5;
//
//            eventPointList.add(eventPoint);
//
//        }
//
//        db.getDaoSave().addListEvent(eventPointList);


        List<EventPoint> eventPointList1 = db.getDaoSave().getListEvent(1);


        List<EventPoint> eventPointList2 = db.getDaoSave().getListEvent(2);


        List<EventPoint> eventAllPointList = db.getDaoSave().getAllListEvent();

        List<Save> list = db.getDaoSave().getListSave();
        for (Save save2 : list) {
            Log.d("EEE", save2.id + "   name " + save2.saveName);
        }


        Log.d("EEE", "List 1");
        for (EventPoint eventPoint1 : eventPointList1) {
            Log.d("EEE", eventPoint1.idPoint + "   idSave " + eventPoint1.idSave);
        }

        Log.d("EEE", "List 2");
        for (EventPoint eventPoint2 : eventPointList2) {
            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
        }

        Log.d("EEE", "List All");
        for (EventPoint eventPoint2 : eventAllPointList) {
            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
        }

        db.getDaoSave().deleteSave(1);


        eventPointList1 = db.getDaoSave().getListEvent(1);


        eventPointList2 = db.getDaoSave().getListEvent(2);


        eventAllPointList = db.getDaoSave().getAllListEvent();

        list = db.getDaoSave().getListSave();
        for (Save save2 : list) {
            Log.d("EEE", save2.id + "   name " + save2.saveName);
        }


        Log.d("EEE", "List 1");
        for (EventPoint eventPoint1 : eventPointList1) {
            Log.d("EEE", eventPoint1.idPoint + "   idSave " + eventPoint1.idSave);
        }

        Log.d("EEE", "List 2");
        for (EventPoint eventPoint2 : eventPointList2) {
            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
        }

        Log.d("EEE", "List All");
        for (EventPoint eventPoint2 : eventAllPointList) {
            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
        }


//        Save saveee = db.getDaoSave().getSave(1);
//        Log.d("EEE", "save id = " + saveee.id);




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
                stopService(new Intent(this, MyService.class));
                super.onBackPressed();

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
