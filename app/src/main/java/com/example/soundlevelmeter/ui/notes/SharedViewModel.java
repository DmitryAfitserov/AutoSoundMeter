package com.example.soundlevelmeter.ui.notes;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedViewModel extends AndroidViewModel implements LifecycleObserver {


    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
    private ArrayList<Note> list;

    public SharedViewModel(@NonNull Application application) {
        super(application);

            getListFromSharedPreferences();

    }


    private void getListFromSharedPreferences() {
        String jsonDataString = preferences.getString("string", null);
        if (jsonDataString != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Note>>() {
            }.getType();

            list = gson.fromJson(jsonDataString, type);
        }


    }

    ArrayList<Note> getList() {
        return list;
    }

    Note getNote(int position) {
        if (position < list.size()) {
            return list.get(position);
        }
        return null;
    }


    private void setListInSharedPreferences() {

        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonDataString = gson.toJson(list);
        editor.putString("string", jsonDataString);


        editor.apply();


    }

    void addNote(Note note) {
        list.add(note);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopActivity() {
        Log.d("EEE", "onStopActivity");
        setListInSharedPreferences();
    }

    void deleteNote(int position) {
        list.remove(position);

    }


}