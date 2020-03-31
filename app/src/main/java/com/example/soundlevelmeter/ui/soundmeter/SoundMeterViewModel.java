package com.example.soundlevelmeter.ui.soundmeter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SoundMeterViewModel extends ViewModel {

    private MutableLiveData<String> valueSpeed;
    private MutableLiveData<String> valueSound;

    public SoundMeterViewModel() {
        valueSpeed = new MutableLiveData<>();
        valueSound = new MutableLiveData<>();
        valueSpeed.setValue("0");
        valueSound.setValue("0");
    }

    LiveData<String> getValueSpeed() {
        return valueSpeed;
    }

    LiveData<String> getValueSound() {
        return valueSound;
    }

    void setValueSpeed(int speed) {
        valueSpeed.setValue(String.valueOf(speed));
    }

    void setValueSound(int sound) {
        valueSound.setValue(String.valueOf(sound));
    }
}