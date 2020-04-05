package com.app.sound_level_meter.ui.notes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.app.sound_level_meter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NoteContentFragment extends Fragment {

    private int position;
    private boolean isNewNote;
    private String nameNote = "";
    private String contentNote = "";
    private SharedViewModel sharedViewModel;
    private Note note;
    private boolean isDeletedNote = false;
    private EditText editTextNameNote;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_note, container, false);

        editTextNameNote = view.findViewById(R.id.edit_text_name_note);
        EditText editTextContentNote = view.findViewById(R.id.edit_text_content_note);
        Button buttonSaveNote = view.findViewById(R.id.button_save_note);
        FloatingActionButton buttonDelete = view.findViewById(R.id.button_delete_note);
        buttonDelete.setOnClickListener(clickListenerDelete);

        buttonSaveNote.setOnClickListener(clickListenerSave);

        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("int");
            getArguments().clear();
            isNewNote = false;
        } else {
            position = -1;
            isNewNote = true;
        }


        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        if (!isNewNote) {
            note = sharedViewModel.getNote(position);
            nameNote = note.getNameNote();
            contentNote = note.getContentNote();
            editTextNameNote.setText(nameNote);
            editTextContentNote.setText(contentNote);
        }

        editTextNameNote.addTextChangedListener(textWatcherName);
        editTextContentNote.addTextChangedListener(textWatcherContent);


//        if (isNewNote) {
//            //  editTextNameNote.requestFocus();
//            //   showKeyboard(editTextNameNote);
//        } else {
//            //  editTextContentNote.requestFocus();
//            //  showKeyboard(editTextContentNote);
//        }
        // showKeyboard();
        //  editTextNameNote.requestFocus();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //   editTextNameNote.requestFocus();

//        Navigation.findNavController(view).
//                addOnDestinationChangedListener(v);

    }

//    private NavController.OnDestinationChangedListener v = new NavController.OnDestinationChangedListener() {
//        @Override
//        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//            if(destination.getId() == R.id.nav_notes){
//                Log.d("EEE", "onDestinationChanged to nav_notes");
//                if (!isDeletedNote) {
//                    save();
//                }
//
//                sharedViewModel = null;
//                note = null;
//
//                Navigation.findNavController(Objects.requireNonNull(getView())).removeOnDestinationChangedListener(v);
//
//
//            }
//
//        }
//
//    };

    private DialogInterface.OnClickListener clickListenerPositiveForAlert = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            deleteNote();
        }
    };

    private DialogInterface.OnClickListener clickListenerNegativeForAlert = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    private View.OnClickListener clickListenerDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDelete();
        }
    };

    private View.OnClickListener clickListenerSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(Objects.requireNonNull(getView())).
                    popBackStack();
        }
    };

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


    private void hideKeyboard() {

//        InputMethodManager inputManager = (InputMethodManager) getView()
//                .getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        IBinder binder = getView().getWindowToken();
//        inputManager.hideSoftInputFromWindow(binder,
//                InputMethodManager.HIDE_NOT_ALWAYS);


        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getView().getRootView().getWindowToken(), 0);



    }

    private void showKeyboard() {

        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager. //showSoftInput(view, 0);
                toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }

    private void showAlertDelete() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialog.setMessage(R.string.message_alert_delete_note);
        alertDialog.setPositiveButton(R.string.yes, clickListenerPositiveForAlert);
        alertDialog.setNegativeButton(R.string.no, clickListenerNegativeForAlert);
        alertDialog.setCancelable(true);
        alertDialog.create().show();

    }

    private void deleteNote() {
        if (position >= 0) {
            sharedViewModel.deleteNote(position);
        }
        isDeletedNote = true;
        Navigation.findNavController(
                Objects.requireNonNull(getView())).popBackStack();
    }


    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.isEmptyList()) {
            Log.d("EEE", "editTextNameNote.isFocused()");
            editTextNameNote.requestFocus();
        }
        showKeyboard();
    }

    @Override
    public void onDestroy() {
        if (!isDeletedNote) {
            save();
        }
        sharedViewModel.setIsUpdateList(true);
        sharedViewModel = null;
        note = null;
        super.onDestroy();

    }
}
