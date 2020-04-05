package com.app.sound_level_meter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.sound_level_meter.R;
import com.app.sound_level_meter.Room.DataEvent;
import com.app.sound_level_meter.Room.MyRoomDataBase;
import com.app.sound_level_meter.Room.Save;
import com.app.sound_level_meter.Singleton.Singleton;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SaveTrackDialog extends Dialog implements View.OnClickListener {


    private String saveName;
    private EditText editText;
    private MyRoomDataBase bd;
    private Set<String> setSave = new HashSet<>();
    private Handler handler = new Handler();
    private Runnable runnable;
    private TextView textViewAlert;
    private boolean canSave = false;

    public SaveTrackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_save_track);

        Button btnSave, btnCancel;
        btnCancel = findViewById(R.id.btn_cancel_save_track);
        btnCancel.setOnClickListener(this);

        btnSave = findViewById(R.id.btn_save_track);
        btnSave.setOnClickListener(this);

        editText = findViewById(R.id.edit_text_name_save);
        editText.addTextChangedListener(textWatcher);
        editText.requestFocus();
        showKeyboard();

        bd = Singleton.getInstance().getBD(getContext());
        getSaveList();

        textViewAlert = findViewById(R.id.text_view_alert);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().isEmpty()) {
                canSave = false;
                textViewAlert.setText(R.string.alert_name_is_empty);
                textViewAlert.setVisibility(View.VISIBLE);
                return;
            }

            if (setSave.contains(s.toString())) {

                canSave = false;
                textViewAlert.setVisibility(View.VISIBLE);
                textViewAlert.setText(R.string.alert_name_is_exist);
                //message
                //  editText.setTextColor();
            } else {
                canSave = true;
                textViewAlert.setVisibility(View.INVISIBLE);

                //drop message
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_save_track: {
                dismiss();
                //test();
                break;
            }
            case R.id.btn_save_track: {
                saveName = editText.getText().toString();
                if (!canSave) {
                    if (textViewAlert.getVisibility() == View.INVISIBLE) {
                        textViewAlert.setVisibility(View.VISIBLE);
                    }

                } else {
                    addSaveInBD();
                    v.setEnabled(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            v.setEnabled(true);
                            dismiss();
                        }
                    };

                }
                break;
            }
            default:
                dismiss();

        }
    }

    private void test() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Log.d("EEE", "List<Save> listSave:");

                List<Save> listSave = bd.getDaoSave().getListSave();

                for (Save save : listSave) {
                    Log.d("EEE", "SaveName =" + save.getSaveName() + "   id = " + save.getId());
                }


                Log.d("EEE", "List<DataEvent>:");
                List<DataEvent> dataEv = bd.getDaoSave().getAllListEvent();

                for (DataEvent dataEvent : dataEv) {

                    Log.d("EEE", "id " + dataEvent.getIdPoint() + "   idSave = " + dataEvent.getIdSave() + "    time =  " + dataEvent.getTime());
                }

            }
        };
        thread.start();


    }

    private void getSaveList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                List<Save> listSave = bd.getDaoSave().getListSave();

                if (!listSave.isEmpty()) {
                    for (Save save : listSave) {
                        setSave.add(save.getSaveName());
                        Log.d("EEE", "setSave.add(save.getSaveName());" + save.getSaveName() + "  id = " + save.getId());
                    }
                }
            }
        };
        thread.start();


    }

    private void addSaveInBD() {

        Thread thread = new Thread() {

            @Override
            public void run() {
                super.run();
                Save save = new Save();
                save.setSaveName(saveName);
                bd.getDaoSave().addSave(save);
                save = bd.getDaoSave().getSaveByName(saveName);
                int saveId = save.getId();
                List<DataEvent> dataEventList = Singleton.getInstance().getList();
                for (DataEvent dataEvent : dataEventList) {

                    dataEvent.setIdSave(saveId);

                }
                bd.getDaoSave().addListEvent(dataEventList);
                handler.post(runnable);
            }
        };
        thread.start();
    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).
                toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).
                toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        closeKeyboard();

    }


}
