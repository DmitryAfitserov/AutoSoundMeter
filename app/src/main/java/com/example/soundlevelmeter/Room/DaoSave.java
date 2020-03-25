package com.example.soundlevelmeter.Room;


import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@androidx.room.Dao
public interface DaoSave {

    @Query("SELECT * FROM Save")
    List<Save> getListSave();

    @Query("SELECT * FROM Save WHERE id = :id")
    Save getSave(int id);



    @Insert
    void addListEvent(List<DataEvent> list);

    @Query("SELECT * FROM DataEvent WHERE idSave = :idSave")
    List<DataEvent> getListEvent(int idSave);

    @Query("SELECT * FROM DataEvent")
    List<DataEvent> getAllListEvent();

    @Query("DELETE FROM Save WHERE id = :id")
    void deleteSave(int id);

    @Query("SELECT * FROM Save WHERE saveName = :saveName")
    Save getSaveByName(String saveName);

    @Insert
    void addSave(Save save);


}
