package com.example.soundlevelmeter.Singleton;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static Singleton instance;
    private boolean isStatusService = false;
    private boolean isStatusSpeedometer = false;
    private boolean isStatusSoundMeter = false;
    private boolean isStatusWriteTrack = false;


    private List<DataEvent> list;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public boolean isStatusService() {
        return isStatusService;
    }

    public void setStatusService(boolean statusService) {
        isStatusService = statusService;
    }

    public boolean isStatusSpeedometer() {
        return isStatusSpeedometer;
    }

    public void setStatusSpeedometer(boolean statusSpeedometer) {
        isStatusSpeedometer = statusSpeedometer;
    }

    public boolean isStatusSoundMeter() {
        return isStatusSoundMeter;
    }

    public void setStatusSoundMeter(boolean statusSoundMeter) {
        isStatusSoundMeter = statusSoundMeter;
    }

    public boolean isStatusWriteTrack() {
        return isStatusWriteTrack;
    }

    public void setStatusWriteTrack(boolean statusWriteTrack) {
        isStatusWriteTrack = statusWriteTrack;
    }

    public List<DataEvent> getList() {
        return list;
    }

    public void addDataToList(DataEvent event) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(event);
    }

    public void clearList() {
        list.clear();
    }
}
