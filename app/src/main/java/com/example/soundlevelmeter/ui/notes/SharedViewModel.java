package com.example.soundlevelmeter.ui.notes;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SharedViewModel extends AndroidViewModel implements LifecycleObserver {


    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
    private ArrayList<Note> list;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        Log.d("EEE", "SharedViewModel");
        if (list == null) {
            getListFromSharedPreferences();
        }

//        for (int i = 0; i < 10; i++) {
//            Note note = new Note(i + "Dima", "dfd");
//            list.add(note);
//        }


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

    public ArrayList<Note> getList() {
        return list;
    }

    public Note getNote(int position) {
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

    public void addNote(Note note) {
        list.add(note);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopActivity() {
        Log.d("EEE", "onStopActivity");
        setListInSharedPreferences();
    }

    public void deleteNote(int position) {
        list.remove(position);

    }


}