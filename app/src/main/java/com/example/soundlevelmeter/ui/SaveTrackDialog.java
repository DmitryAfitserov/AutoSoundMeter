package com.example.soundlevelmeter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Room.DataEvent;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Singleton.Singleton;

import java.util.List;

public class SaveTrackDialog extends Dialog implements View.OnClickListener {


    private Button btnSave, btnCancel;

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




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_save_track: {
                dismiss();
                break;
            }
            case R.id.btn_save_track: {
                MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());
                List<DataEvent> list = Singleton.getInstance().getList();

                break;
            }
            default:
                dismiss();

        }
    }
}
