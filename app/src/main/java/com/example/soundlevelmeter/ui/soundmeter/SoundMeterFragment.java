package com.example.soundlevelmeter.ui.soundmeter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.soundlevelmeter.Interface.CallBackFromService;
import com.example.soundlevelmeter.MyService;
import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Singleton.Singleton;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.LOCATION_SERVICE;

public class SoundMeterFragment extends Fragment implements CallBackFromService {


    private static final int RECORD_AUDIO_REQUEST_CODE = 1001;
    private final int LOCATION_REQUEST_CODE = 1002;
    private Button btnSpeedometer;
    private SoundMeterViewModel soundMeterViewModel;
    private LocationManager locationManager;
    private ServiceConnection serviceConnection;
    private Intent intent;
    private TextView textViewSpeedometer;
    private TextView textViewSoundMeter;
    private MyService myService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        soundMeterViewModel =
                ViewModelProviders.of(this).get(SoundMeterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sound_meter, container, false);
        textViewSpeedometer = root.findViewById(R.id.text_speedometer);
        textViewSoundMeter = root.findViewById(R.id.text_sound_meter);
        btnSpeedometer = root.findViewById(R.id.button_speedometer);
        final Button btnStartTrack = root.findViewById(R.id.button_start_track);

        soundMeterViewModel.getValueSpeed().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewSpeedometer.setText(s);
            }
        });
        soundMeterViewModel.getValueSound().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewSoundMeter.setText(s);
            }
        });
        if (Singleton.getInstance().isStatusSpeedometer()) {
            btnSpeedometer.setText(R.string.button_stop_speedometer);
        }
        if (Singleton.getInstance().isStatusWriteTrack()) {
            btnStartTrack.setText(R.string.button_stop_track);
        }
        btnSpeedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkGps()) {
                    clickBtnSpeedometer();
                }
            }
        });

        btnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Singleton.getInstance().isStatusWriteTrack()) {
                    if (Singleton.getInstance().isStatusSpeedometer() && Singleton.getInstance().isStatusSoundMeter()) {
                        Singleton.getInstance().clearList();
                        Singleton.getInstance().setStatusWriteTrack(true);
                        btnStartTrack.setText(R.string.button_stop_track);

                    }
                    if (!Singleton.getInstance().isStatusSpeedometer()) {
                        Toast.makeText(getContext(), "Not speed", Toast.LENGTH_LONG).show();
                    }
                    if (!Singleton.getInstance().isStatusSoundMeter()) {
                        Toast.makeText(getContext(), "Not sound", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Singleton.getInstance().setStatusWriteTrack(false);
                    btnStartTrack.setText(R.string.button_start_track);
                }
            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("EEE", " onServiceConnected OK  ");
                MyService.LocalBinder binderService = (MyService.LocalBinder) service;
                binderService.setCallBackFromService(SoundMeterFragment.this);
                myService = ((MyService.LocalBinder) service).getService();
                myService.startSoundMeter();
                Singleton.getInstance().setStatusService(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("EEE", " onServiceDisconnected  Disconnected ");
                Singleton.getInstance().setStatusService(false);

            }

        };


        if (checkPermissionSoundMeter()) {
            createService();
            Singleton.getInstance().setStatusSoundMeter(true);
        }


        return root;
    }


    private void clickBtnSpeedometer() {
        if (Singleton.getInstance().isStatusSpeedometer()) {
            myService.stopSpeedometer();
            btnSpeedometer.setText(R.string.button_start_speedometer);
            Singleton.getInstance().setStatusSpeedometer(false);
        } else {
            if (checkPermission()) {
                myService.startSpeedometer();
                Singleton.getInstance().setStatusSpeedometer(true);
                btnSpeedometer.setText(R.string.button_stop_speedometer);
            }

        }
    }

    private void createService() {
        Log.d("EEE", " createService  ");
        intent = new Intent(getContext(), MyService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void callBackFromSoundMeter(int sound) {

        soundMeterViewModel.setValueSound(sound);
    }

    @Override
    public void callBackFromSpeedometer(int speed) {
        soundMeterViewModel.setValueSpeed(speed);
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean checkGps() {

        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
            // reuqest for permission

            return false;

        } else {
            return true;
            // already permission granted
        }
    }

    private boolean checkPermissionSoundMeter() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_REQUEST_CODE);

        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (RECORD_AUDIO_REQUEST_CODE == requestCode && permissions[0].equals(Manifest.permission.RECORD_AUDIO)) {
            if (grantResults[0] == 0) {
                Singleton.getInstance().setStatusSoundMeter(true);
            } else {
                Toast.makeText(getContext(), R.string.not_permission, Toast.LENGTH_SHORT).show();
            }
            if (!Singleton.getInstance().isStatusSoundMeter()) {
                createService();
            }
        }

        if (LOCATION_REQUEST_CODE == requestCode) {
            if (grantResults[0] == 0 && grantResults[1] == 0) {
                clickBtnSpeedometer();
            } else {
                Toast.makeText(getContext(), R.string.not_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }


}