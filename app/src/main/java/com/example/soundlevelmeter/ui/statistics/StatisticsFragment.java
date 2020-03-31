package com.example.soundlevelmeter.ui.statistics;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.soundlevelmeter.Interface.CallBackForStaticsits;
import com.example.soundlevelmeter.MyService;
import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Room.DataEvent;
import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Room.Save;
import com.example.soundlevelmeter.Singleton.Singleton;
import com.example.soundlevelmeter.ui.SaveTrackDialog;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.BIND_AUTO_CREATE;

public class StatisticsFragment extends Fragment implements View.OnClickListener, CallBackForStaticsits {

    private List<DataEvent> list;
    private List<Save> listSave;
    private CheckBox checkBoxSpeed;
    private CheckBox checkBoxSound;
    private GraphView graph;
    private LineGraphSeries<DataPoint> seriesSpeed;
    private LineGraphSeries<DataPoint> seriesSound;
    private final double coef = 0.621d;
    private boolean isUseMph;
    private Handler handler = new Handler();
    private Button btnPlayStop;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        isUseMph = Singleton.getInstance().isUseMPH();

        graph = root.findViewById(R.id.graph_view);
        graph.setVisibility(View.VISIBLE);
        // graph.getGridLabelRenderer().setHorizontalAxisTitle(titleTime);

        checkBoxSpeed = root.findViewById(R.id.checkbox_speed);
        checkBoxSound = root.findViewById(R.id.checkbox_sound);

        btnPlayStop = root.findViewById(R.id.btn_play_stop);
        btnPlayStop.setOnClickListener(this);

        Button btnClean = root.findViewById(R.id.btn_clean);
        btnClean.setOnClickListener(this);

        Button btnSave = root.findViewById(R.id.btn_save_track_in_statistics);
        btnSave.setOnClickListener(this);

        Button btnOpenSaveTrack = root.findViewById(R.id.btn_open_save_track);
        btnOpenSaveTrack.setOnClickListener(this);

        createListeners();

        if (!Singleton.getInstance().isCheckBoxSound()) {
            checkBoxSound.setChecked(false);
        }
        if (!Singleton.getInstance().isCheckBoxSpeed()) {
            checkBoxSpeed.setChecked(false);
        }
        if (Singleton.getInstance().isStatusWriteTrack()) {
            btnPlayStop.setText(R.string.button_pause);
        }

        createService();
        upDateGraph();

        return root;
    }

    private void createService() {
        Intent intent = new Intent(getContext(), MyService.class);
        Objects.requireNonNull(getActivity()).bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    private void upDateGraph() {
        list = Singleton.getInstance().getList();
        if (list == null) {
            return;
        }
        graph.removeAllSeries();
        if (checkBoxSpeed.isChecked()) {
            startPainGraphSpeed();
        }
        if (checkBoxSound.isChecked()) {
            startPainGraphSound();
        }
        if (checkBoxSound.isChecked() || checkBoxSpeed.isChecked()) {
            settingScaleGraph();

        }
    }

    private void settingScaleGraph() {
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        graph.getViewport().setMinX(20);
        graph.getViewport().setMaxX(50);

        graph.getViewport().scrollToEnd();
    }

    private void startPainGraphSound() {

            try {
                int size = list.size();
                DataPoint[] dataPoints = new DataPoint[size];

                for (int i = 0; i < size; i++) {

                    double x = list.get(i).getTime() / 10d;
                    double y = list.get(i).getSound();

                    dataPoints[i] = new DataPoint(x, y);

                }
                seriesSound = new LineGraphSeries<>(dataPoints);

                graph.addSeries(seriesSound);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
    }

    private void startPainGraphSpeed() {

        try {
            int size = list.size();
            DataPoint[] dataPoints = new DataPoint[size];

            for (int i = 0; i < size; i++) {

                double x = list.get(i).getTime() / 10d;
                double y = list.get(i).getSpeed();
                if (isUseMph) {
                    y = y * coef;
                    Log.d("EEE", "speed " + list.get(i).getSpeed() + " newspeed " + y);
                }
                dataPoints[i] = new DataPoint(x, y);
                //   Log.d("EEE", "x=" + x + "     y=" + y);
            }
            seriesSpeed = new LineGraphSeries<>(dataPoints);

            graph.addSeries(seriesSpeed);

        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void callBackForUpDataGraph() {
        upDateGraph();
    }

    private void createListeners() {

        checkBoxSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setCheckBoxSound(checkBoxSound.isChecked());
                upDateGraph();
            }
        });
        checkBoxSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setCheckBoxSpeed(checkBoxSpeed.isChecked());
                upDateGraph();
            }
        });

    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("EEE", " onServiceConnected with Statistics OK  ");
            MyService.LocalBinder binderService = (MyService.LocalBinder) service;
            binderService.setCallBackForStatistics(StatisticsFragment.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("EEE", " onServiceDisconnected with Statistics  Disconnected ");

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_stop: {
                eventClickBtnPlayStop();

                break;
            }
            case R.id.btn_clean: {

                Singleton.getInstance().clearList();
                if (!Singleton.getInstance().isStatusWriteTrack()) {
                    upDateGraph();
                }

                break;
            }
            case R.id.btn_save_track_in_statistics: {

                if (Singleton.getInstance().isStatusWriteTrack()) {
                    eventClickBtnPlayStop();
                    SaveTrackDialog saveTrackDialog =
                            new SaveTrackDialog(Objects.requireNonNull(getContext()));
                    saveTrackDialog.show();
                } else if (Singleton.getInstance().getList().isEmpty()) {
                    Toast.makeText(getContext(), R.string.toast_not_wrote_track, Toast.LENGTH_SHORT).show();
                } else {
                    SaveTrackDialog saveTrackDialog =
                            new SaveTrackDialog(Objects.requireNonNull(getContext()));
                    saveTrackDialog.show();
                }

                break;
            }

            case R.id.btn_open_save_track: {

                final Runnable runnableUpdateUI = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("EEE", "  = updateUI = ");
                        upDateGraph();
                    }
                };


                Runnable runnable = new Runnable() {

                    int positionItem = 0;
                    ArrayAdapter<String> adapter;
                    ArrayList<String> stringsSave = new ArrayList<>();
                    AlertDialog alertDialog;

                    @Override
                    public void run() {



                        for (int i = 0; i < listSave.size(); i++) {
                            stringsSave.add(listSave.get(i).getSaveName());
                        }
                        adapter = new ArrayAdapter<>(Objects.requireNonNull(
                                getContext()), android.R.layout.simple_list_item_single_choice, stringsSave);

                        View.OnClickListener btnNeutDelete = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!stringsSave.isEmpty()) {
                                    deleteSaveFromBD(listSave.get(positionItem).getId());
                                    listSave.remove(positionItem);
                                    Log.d("EEE", "which = " + positionItem + "  listSave.size() =  " + listSave.size());
                                    stringsSave.remove(positionItem);
                                    if (positionItem > stringsSave.size() - 1) {
                                        positionItem--;
                                    }
                                    alertDialog.getListView().setItemChecked(positionItem, true);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        };

                        alertDialog = new
                                AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                .setTitle(R.string.dialog_message_choose_save)
                                .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        positionItem = which;
                                    }
                                })
                                .setPositiveButton(R.string.button_open_save_track, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!listSave.isEmpty()) {
                                            int id = listSave.get(positionItem).getId();

                                            if (Singleton.getInstance().isStatusWriteTrack()) {
                                                eventClickBtnPlayStop();
                                            }
                                            Log.d("EEE", "positionItem = " + positionItem + " id = " + id);
                                            openEventsFromBD(id, runnableUpdateUI);
                                        }

                                    }
                                })
                                .setNegativeButton(R.string.button_close, null)
                                .setNeutralButton(R.string.btn_delete_note, null)
                                .create();


                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(btnNeutDelete);

                    }
                };

                getSaveList(runnable);

                break;
            }


        }
    }

    private void eventClickBtnPlayStop() {

        if (!Singleton.getInstance().isStatusWriteTrack()) {
            if (Singleton.getInstance().isStatusSpeedometer() &&
                    Singleton.getInstance().isStatusSoundMeter()) {
                Singleton.getInstance().setStatusWriteTrack(true);
                btnPlayStop.setText(R.string.button_pause);
            } else {
                if (!Singleton.getInstance().isStatusSpeedometer()) {
                    Toast.makeText(getContext(), R.string.text_for_not_work_speedometer,
                            Toast.LENGTH_SHORT).show();
                }
                if (!Singleton.getInstance().isStatusSoundMeter()) {
                    Toast.makeText(getContext(), R.string.text_for_not_work_soundmeter,
                            Toast.LENGTH_SHORT).show();
                }
            }


        } else {
            Singleton.getInstance().setStatusWriteTrack(false);
            btnPlayStop.setText(R.string.button_play);
        }


    }

    private void openEventsFromBD(final int idSave, final Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());

                list.clear();
                list = bd.getDaoSave().getListEvent(idSave);
                Singleton.getInstance().setList(list);
                Log.d("EEE", "list.size = " + list.size());
                handler.post(runnable);

            }
        };
        thread.start();
    }

    private void deleteSaveFromBD(final int idSave) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());
                bd.getDaoSave().deleteSave(idSave);
            }
        };
        thread.start();
    }


    private void getSaveList(final Runnable runnable) {
        final MyRoomDataBase bd = Singleton.getInstance().getBD(getContext());
        Thread thread = new Thread() {
            @Override
            public void run() {
                listSave = bd.getDaoSave().getListSave();
                handler.post(runnable);
            }
        };
        thread.start();
    }
}