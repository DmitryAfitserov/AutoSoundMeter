package com.example.soundlevelmeter.ui.statistics;

import android.os.Bundle;
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

import com.example.soundlevelmeter.MainActivity;
import com.example.soundlevelmeter.R;
import com.example.soundlevelmeter.Singleton.DataEvent;
import com.example.soundlevelmeter.Singleton.Singleton;
import com.google.android.gms.location.LocationCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    private List<DataEvent> list;
    private CheckBox checkBoxSpeed;
    private CheckBox checkBoxSound;
    private GraphView graph;

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
            }
        });
        checkBoxSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setCheckBoxSpeed(checkBoxSpeed.isChecked());
            }
        });


        if (checkBoxSound.isChecked()) {
            startPainGraphSound(true);
        }
        if (checkBoxSpeed.isChecked()) {
            startPainGraphSpeed();
        }


        //    getCorrectList();


        return root;
    }

    private void startPainGraphSound(boolean isNewGraph) {

        list = Singleton.getInstance().getList();
        if (list == null) {
            return;
        }
        if (isNewGraph) {

            try {
                DataPoint[] dataPoints = new DataPoint[list.size()];

                for (int i = 0; i < list.size(); i++) {

                    double x = list.get(i).getTime() / 10d;
                    double y = list.get(i).getSound();
                    dataPoints[i] = new DataPoint(x, y);
                    Log.d("EEE", "x=" + x + "     y=" + y);
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);


                graph.addSeries(series);
                graph.getViewport().scrollToEnd();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {

        }

    }

    private void startPainGraphSpeed() {


    }




}