package com.app.sound_level_meter.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Save {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String saveName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }
}
