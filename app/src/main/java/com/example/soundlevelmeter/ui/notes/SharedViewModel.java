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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SharedViewModel extends AndroidViewModel {


    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
    ArrayList<Note> list;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Note note = new Note(i + "Dima", "dfd");
            list.add(note);
        }
        setSetSharedPreferences();
        getSetSharedPreferences();
        for (Note note : list) {

            Log.d("EEE", note.getNameNote() + "   " + note.getContentNote());

        }


    }




    private void getSetSharedPreferences() {
        String jsonDataString = preferences.getString("string", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();

        list = gson.fromJson(jsonDataString, type);

    }

    private void setSetSharedPreferences() {

        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonDataString = gson.toJson(list);
        editor.putString("string", jsonDataString);


        editor.apply();
        list = null;

    }

}