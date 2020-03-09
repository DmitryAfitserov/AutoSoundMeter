package com.example.soundlevelmeter.ui.notes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

public class AdapterForListNotes extends ArrayAdapter<Note> {


    public AdapterForListNotes(@NonNull Context context, int resource) {
        super(context, resource);
    }


}
