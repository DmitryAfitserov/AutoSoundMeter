package com.example.soundlevelmeter.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Save.class,
        parentColumns = "id", childColumns = "idSave", onDelete = CASCADE, onUpdate = CASCADE), primaryKeys = {"idPoint"}, tableName = "event_point")
public class EventPoint {


    public long idPoint;

    @ColumnInfo(name = "idSave", index = true)
    public int idSave;


    public int sound;
    public int speed;
    public long time;

}
