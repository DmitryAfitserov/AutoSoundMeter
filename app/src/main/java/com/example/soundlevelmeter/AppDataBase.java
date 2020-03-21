package com.example.soundlevelmeter;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Room.Save;

public class AppDataBase extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

//
//        MyRoomDataBase db = Room.databaseBuilder(getApplicationContext(),
//                MyRoomDataBase.class, "database-name").build();
//
//        Save save = new Save();
//        save.saveName = "SAVE 1";
//        db.getDaoSave().addSave(save);
//
//
//        Save save1 = db.getDaoSave().getSave(0);
//        Log.d("EEE", "save = "  + save1.saveName);
//
//        //  EventList eventList = new EventList();
//        //    eventList.save = new Save();
//        //    eventList.save.saveName = "Save 1";
////        List<EventPoint> list = new ArrayList<>();
////        for(int i = 0; i < 10;i++){
////            EventPoint eventPoint = new EventPoint();
////            eventPoint.idPoint = i;
////            eventPoint.idSave = 0;
////            eventPoint.sound = i;
////            eventPoint.speed = i;
////            eventPoint.time = i;
////            list.add(eventPoint);
////        }
////        eventList.listEvent = list;
////
////        db.getDaoSave().setSaveWithList(eventList);
//
////        LiveData<EventList> eventList1 = db.getDaoSave().getSaveWithList(0);
//
////        Log.d("EEE", eventList1.getValue().save.saveName + "    " +  eventList1.getValue().listEvent.get(4).idPoint);


    }
}
