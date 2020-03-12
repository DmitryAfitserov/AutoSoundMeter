package com.example.soundlevelmeter.ui.notes;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.util.LinkedHashSet;
import java.util.Set;

public class SharedViewModel extends AndroidViewModel {

    private final MutableLiveData<Note> dataNotes = new MutableLiveData<>();
    Set<String> dataNotesSet;
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

    public SharedViewModel(@NonNull Application application) {
        super(application);
        setSetSharedPreferences();
        getSetSharedPreferences();
        String[] t = {};
        if (dataNotesSet == null) {
            Log.d("EEE", "null dataset");
            return;
        }

        t = dataNotesSet.toArray(new String[dataNotesSet.size()]);
        for (int i = 0; i < t.length; i++) {

            Log.d("EEE", t[i]);
        }
    }


    public MutableLiveData<Note> getData() {


        return dataNotes;
    }

    private void getSetSharedPreferences() {
        dataNotesSet = preferences.getStringSet("stringSet", null);
    }

    private void setSetSharedPreferences() {
        dataNotesSet = new LinkedHashSet<>();

        for (int i = 0; i < 10; i++) {

            dataNotesSet.add(" i the best");

        }


        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("stringSet", dataNotesSet);

        editor.apply();

        dataNotesSet = new LinkedHashSet<>();
    }

}