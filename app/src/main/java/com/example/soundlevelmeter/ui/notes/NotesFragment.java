package com.example.soundlevelmeter.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.soundlevelmeter.R;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends ListFragment {

    private NotesViewModel notesViewModel;
    private ArrayList<Note> list = new ArrayList<>();




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            Note note = new Note("dfdf", "dfdf");
            list.add(note);

        }


        AdapterForListNotes adapter =
                new AdapterForListNotes(getContext(), R.layout.item_list_fragment, list);
        setListAdapter(adapter);
    }
}