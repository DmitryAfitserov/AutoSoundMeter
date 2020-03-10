package com.example.soundlevelmeter.Singleton;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static Singleton instance;
    private boolean isStatusService = false;
    private boolean isStatusSpeedometer = false;
    private boolean isStatusSoundMeter = false;
    private boolean isStatusWriteTrack = false;
    private boolean isCheckBoxSpeed = true;
    private boolean isCheckBoxSound = true;
    private boolean isUseMPH;
    private long startTime;


    private List<DataEvent> list;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public boolean isUseMPH() {
        return isUseMPH;
    }

    public void setUseMPH(boolean useMPH) {
        isUseMPH = useMPH;
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

    public boolean isCheckBoxSpeed() {
        return isCheckBoxSpeed;
    }

    public void setCheckBoxSpeed(boolean checkBoxSpeed) {
        isCheckBoxSpeed = checkBoxSpeed;
    }

    public boolean isCheckBoxSound() {
        return isCheckBoxSound;
    }

    public void setCheckBoxSound(boolean checkBoxSound) {
        isCheckBoxSound = checkBoxSound;
    }

    public List<DataEvent> getList() {
        return list;
    }

    public void addDataToList(DataEvent event) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list.size() == 0) {
            startTime = event.getTime();
        }
        long temp = Math.round((event.getTime() - startTime) / 100d);

        event.setTime(temp);
        list.add(event);
    }

    public void clearList() {
        if (list != null) {
            list.clear();
        }

    }

    public void destroy() {
        instance = null;
    }
}
