package com.example.soundlevelmeter.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Save {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String saveName;

}
