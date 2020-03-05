package com.example.soundlevelmeter.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    List<DataEvent> list;

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
        getCorrectList();
        final GraphView graph = root.findViewById(R.id.graph_view);
        graph.setVisibility(View.VISIBLE);

        try {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, 1),
                    new DataPoint(Integer.valueOf(1), Integer.valueOf(2)),
                    new DataPoint(Integer.valueOf(2), Integer.valueOf(5)),
                    new DataPoint(Integer.valueOf(3), Integer.valueOf(7)),
                    new DataPoint(Integer.valueOf(4), Integer.valueOf(9))
            });
            graph.addSeries(series);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        return root;
    }

    private void getCorrectList() {
        if (list == null) {
            list = Singleton.getInstance().getList();
        }
        if (list.size() < 1) {
            list = Singleton.getInstance().getList();
        }

        long startTime = list.get(0).getTime();
        for (DataEvent event : list) {
            long currentTime = event.getTime();
            event.setTime(currentTime - startTime);
            Log.d("EEE", "time " + event.getTime());
        }

    }

}