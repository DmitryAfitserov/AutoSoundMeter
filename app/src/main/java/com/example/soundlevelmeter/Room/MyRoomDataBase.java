package com.example.soundlevelmeter.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Save.class, DataEvent.class}, version = 1, exportSchema = false)
public abstract class MyRoomDataBase extends RoomDatabase {

    public abstract DaoSave getDaoSave();
}
