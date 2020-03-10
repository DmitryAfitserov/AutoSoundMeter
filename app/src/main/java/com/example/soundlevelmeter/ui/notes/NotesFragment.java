package com.example.soundlevelmeter.ui.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;
import com.example.soundlevelmeter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class NotesFragment extends ListFragment implements AbsListView.OnScrollListener {

    private ArrayList<Note> list = new ArrayList<>();
    private View view;
    private FloatingActionButton fab;






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 5; i++) {
            Note note = new Note("dfdf", "dfdf");
            list.add(note);

        }

        AdapterForListNotes adapter =
                new AdapterForListNotes(getContext(), R.layout.item_list_fragment, list);
        setListAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notes, container, false);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EEE", "floating button click");
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(this);

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Navigation.findNavController(getView()).navigate(R.id.fragment_note);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int btn_initPosY = fab.getScrollY();
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            fab.animate().cancel();
            fab.animate().translationYBy(150);
        } else {
            fab.animate().cancel();
            fab.animate().translationY(btn_initPosY);
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}