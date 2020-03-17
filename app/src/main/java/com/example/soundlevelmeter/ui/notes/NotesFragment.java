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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.example.soundlevelmeter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Objects;


public class NotesFragment extends ListFragment implements AbsListView.OnScrollListener {

    private FloatingActionButton fab;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        for (int i = 0; i < 5; i++) {
//            Note note = new Note(i + "  dfdf", "dfdf");
//            list.add(note);
//
//        }

        SharedViewModel sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        Objects.requireNonNull(getActivity()).getLifecycle().addObserver(sharedViewModel);
        ArrayList<Note> list;
        list = sharedViewModel.getList();
        if (getListAdapter() == null) {
            AdapterForListNotes adapter =
                    new AdapterForListNotes(Objects.requireNonNull(getContext()), R.layout.item_list_fragment, list);
            setListAdapter(adapter);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();
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
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Log.d("EEE", "touch");
        changeFragment(position);

    }

    private void changeFragment() {
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.fragment_note);
    }

    private void changeFragment(int position) {
        Bundle args = new Bundle();
        args.putInt("int", position);

        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.fragment_note, args);
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