package com.example.soundlevelmeter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Singleton.Singleton;

public class SaveTrackDialog extends Dialog implements DialogInterface.OnClickListener {


    public SaveTrackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_save_track);



    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case R.id.btn_cancel_save_track: {
                Log.d("EEE", "R.id.btn_cancel_save_track");
                dialog.cancel();
                break;
            }
            case R.id.btn_save_track: {
                Log.d("EEE", "R.id.btn_cancel_save_track");
                MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());

                break;
            }
            default:
                dialog.cancel();


        }
    }
}
