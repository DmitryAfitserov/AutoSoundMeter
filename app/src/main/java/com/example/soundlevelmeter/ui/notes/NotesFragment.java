package com.example.soundlevelmeter.ui.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;


import com.example.soundlevelmeter.R;

import java.util.ArrayList;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("EEE", "position  " + position);

//        NoteContentFragment fragmentNote = new NoteContentFragment(list.get(position));
//
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack if needed
//        transaction.replace(R.id.nav_host_fragment, fragmentNote).setPrimaryNavigationFragment(fragmentNote);
//        transaction.hide(this);
//        transaction.addToBackStack(null);
//
//// Commit the transaction
//        transaction.commit();

        Navigation.findNavController(getView()).navigate(R.id.fragment_note);


    }
}