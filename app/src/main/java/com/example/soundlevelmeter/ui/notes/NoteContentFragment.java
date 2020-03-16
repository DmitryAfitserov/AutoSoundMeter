package com.example.soundlevelmeter.ui.notes;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.soundlevelmeter.MainActivity;
import com.example.soundlevelmeter.R;

public class NoteContentFragment extends Fragment {

    private int position;
    private boolean isNewNote;
    private String nameNote = "";
    private String contentNote = "";
    SharedViewModel sharedViewModel;
    Note note;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_note, container, false);

        EditText editTextNameNote = view.findViewById(R.id.edit_text_name_note);
        EditText editTextContentNote = view.findViewById(R.id.edit_text_content_note);

        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("int");
            getArguments().clear();
            isNewNote = false;
        } else {
            isNewNote = true;
        }

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        if (isNewNote) {

        } else {
            note = sharedViewModel.getNote(position);
            Log.d("EEE", note.getNameNote());
            nameNote = note.getNameNote();
            contentNote = note.getContentNote();
            editTextNameNote.setText(nameNote);
            editTextContentNote.setText(contentNote);
        }

        editTextNameNote.addTextChangedListener(textWatcherName);
        editTextContentNote.addTextChangedListener(textWatcherContent);

        return view;
    }

    private TextWatcher textWatcherName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            nameNote = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher textWatcherContent = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            contentNote = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void save() {
        if (isNewNote) {
            if (!nameNote.isEmpty() || !contentNote.isEmpty()) {
                Note note = new Note(nameNote, contentNote);
                sharedViewModel.addNote(note);
            }
        } else {
            note.setNameNote(nameNote);
            note.setContentNote(contentNote);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        save();
        hideKeyboard();

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
