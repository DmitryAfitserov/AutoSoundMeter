package com.example.soundlevelmeter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Singleton.Singleton;

public class SaveTrackDialog extends Dialog {


    public SaveTrackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_save_track);
        MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());


    }
}
