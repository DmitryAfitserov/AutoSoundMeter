package com.example.soundlevelmeter.ui.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.soundlevelmeter.R;

public class NoteContentFragment extends Fragment {

    private int position;
    private boolean isNewNote;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("int");
            getArguments().clear();
            isNewNote = false;
        } else {
            isNewNote = true;
        }



        return inflater.inflate(R.layout.fragment_content_note, container, false);
    }
}
