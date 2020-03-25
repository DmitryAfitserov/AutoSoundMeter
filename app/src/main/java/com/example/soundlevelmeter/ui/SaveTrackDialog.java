package com.example.soundlevelmeter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Room.DataEvent;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Room.Save;
import com.example.soundlevelmeter.Singleton.Singleton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveTrackDialog extends Dialog implements View.OnClickListener {


    private Button btnSave, btnCancel;
    private String saveName;
    private EditText editText;
    private MyRoomDataBase bd;
    private Set<String> setSave = new HashSet<>();

    public SaveTrackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_save_track);
        btnCancel = findViewById(R.id.btn_cancel_save_track);
        btnCancel.setOnClickListener(this);

        btnSave = findViewById(R.id.btn_save_track);
        btnSave.setOnClickListener(this);

        editText = findViewById(R.id.edit_text_name_save);
        editText.addTextChangedListener(textWatcher);

        bd = Singleton.getInstance().getBD(getContext());
        getSaveList();



    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_save_track: {
                dismiss();
                break;
            }
            case R.id.btn_save_track: {
                saveName = editText.getText().toString();
                if (saveName.isEmpty()) {
                    Toast.makeText(getContext(),
                            R.string.toast_input_name_track, Toast.LENGTH_SHORT).show();
                } else {

                    List<DataEvent> list = Singleton.getInstance().getList();
                    Save save = new Save();
                }



                break;
            }
            default:
                dismiss();

        }
    }

    private void getSaveList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                List<Save> listSave = bd.getDaoSave().getListSave();

                if (listSave.isEmpty()) {
                    Log.d("EEE", "listSave.isEmpty()");
                } else {
                    for (Save save : listSave) {
                        setSave.add(save.getSaveName());
                        Log.d("EEE", "setSave.add(save.getSaveName());" + save.getSaveName());
                    }
                }
            }
        };
        thread.start();


    }
}
