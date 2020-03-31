package com.example.soundlevelmeter.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Save.class,
        parentColumns = "id", childColumns = "idSave", onDelete = CASCADE, onUpdate = CASCADE), tableName = "DataEvent")
public class DataEvent {

    @PrimaryKey(autoGenerate = true)
    private long idPoint;

    @ColumnInfo(name = "idSave", index = true)
    private int idSave;


    private int sound;
    private int speed;
    private long time;

    public long getIdPoint() {
        return idPoint;
    }

    void setIdPoint(long idPoint) {
        this.idPoint = idPoint;
    }

    public int getIdSave() {
        return idSave;
    }

    public void setIdSave(int idSave) {
        this.idSave = idSave;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
