package com.example.soundlevelmeter.Room;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Save.class,
        parentColumns = "idSave", childColumns = "idPoint", onDelete = CASCADE))
public class EventPoint {

    @PrimaryKey
    public int idSave;

    @PrimaryKey(autoGenerate = true)
    private long idPoint;
    private int sound;
    private int speed;
    private long time;

}
