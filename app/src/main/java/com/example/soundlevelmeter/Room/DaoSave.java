package com.example.soundlevelmeter.Room;


import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@androidx.room.Dao
public interface DaoSave {

    @Query("SELECT * FROM Save")
    List<Save> getListSave();

    @Query("SELECT * FROM Save WHERE id = :id")
    Save getSave(long id);

    @Transaction
    @Query("SELECT * FROM SAVE WHERE id = :idSave")
    LiveData<EventList> getSaveWithList(int idSave);



    @Insert
    void addSave(Save save);

    @Delete
    void deleteSave(Save save);


}
