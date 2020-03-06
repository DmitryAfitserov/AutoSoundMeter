package com.example.soundlevelmeter.ui.statistics;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.soundlevelmeter.Interface.CallBackForStaticsits;
import com.example.soundlevelmeter.MainActivity;
import com.example.soundlevelmeter.MyService;
import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Singleton.DataEvent;
import com.example.soundlevelmeter.Singleton.Singleton;
import com.example.soundlevelmeter.ui.soundmeter.SoundMeterFragment;
import com.google.android.gms.location.LocationCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class StatisticsFragment extends Fragment implements CallBackForStaticsits {

    private StatisticsViewModel statisticsViewModel;
    private List<DataEvent> list;
    private CheckBox checkBoxSpeed;
    private CheckBox checkBoxSound;
    private GraphView graph;
    private LineGraphSeries<DataPoint> seriesSpeed;
    private LineGraphSeries<DataPoint> seriesSound;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel =
                ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
//        final TextView textView = root.findViewById(R.id.text_statistics);
//        statisticsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        graph = root.findViewById(R.id.graph_view);
        graph.setVisibility(View.VISIBLE);
        //     graph.getViewport().setScalable(true);

        checkBoxSpeed = root.findViewById(R.id.checkbox_speed);
        checkBoxSound = root.findViewById(R.id.checkbox_sound);

        if (Singleton.getInstance().isCheckBoxSound()) {
            checkBoxSound.setChecked(true);
        } else checkBoxSound.setChecked(false);

        if (Singleton.getInstance().isCheckBoxSpeed()) {
            checkBoxSpeed.setChecked(true);
        } else checkBoxSpeed.setChecked(false);

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
        createService();

        upDateGraph();

        return root;
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

    private void createService() {
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    private void upDateGraph() {
        list = Singleton.getInstance().getList();
        if (list == null) {
            return;
        }
        graph.removeAllSeries();
        //  graph.getViewport().setXAxisBoundsManual(true);
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
        //   graph.getViewport().setScrollable(true);

        //   graph.getViewport().setMinX(15);
        //  graph.getViewport().setMaxX(30);

        //   graph.getViewport().setXAxisBoundsManual(true);
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
                    Log.d("EEE", "x=" + x + "     y=" + y);
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
                dataPoints[i] = new DataPoint(x, y);
                Log.d("EEE", "x=" + x + "     y=" + y);
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
}