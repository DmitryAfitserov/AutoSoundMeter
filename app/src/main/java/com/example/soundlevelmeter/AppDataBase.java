package com.example.soundlevelmeter;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.soundlevelmeter.Room.MyRoomDataBase;
import com.example.soundlevelmeter.Room.Save;

public class AppDataBase {

    MyRoomDataBase bd;

    public AppDataBase(Context context) {

        bd = Room.databaseBuilder(context,
                MyRoomDataBase.class, "database-name").build();

    }


//
//        MyRoomDataBase db = Room.databaseBuilder(getApplicationContext(),
//                MyRoomDataBase.class, "database-name").allowMainThreadQueries().build();

//        Save save = new Save();
//        save.saveName = "SAVE 1";
//        db.getDaoSave().addSave(save);

//        List<EventPoint> eventPointList = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            EventPoint eventPoint = new EventPoint();
//            if(i < 10){
//                eventPoint.idSave = 1;
//            } else {
//                eventPoint.idSave = 2;
//            }
//            eventPoint.time = 5;
//
//            eventPointList.add(eventPoint);
//
//        }
//
//        db.getDaoSave().addListEvent(eventPointList);

//
//        List<EventPoint> eventPointList1 = db.getDaoSave().getListEvent(1);
//
//
//        List<EventPoint> eventPointList2 = db.getDaoSave().getListEvent(2);
//
//
//        List<EventPoint> eventAllPointList = db.getDaoSave().getAllListEvent();
//
//        List<Save> list = db.getDaoSave().getListSave();
//        for (Save save2 : list) {
//            Log.d("EEE", save2.id + "   name " + save2.saveName);
//        }
//
//
//        Log.d("EEE", "List 1");
//        for (EventPoint eventPoint1 : eventPointList1) {
//            Log.d("EEE", eventPoint1.idPoint + "   idSave " + eventPoint1.idSave);
//        }
//
//        Log.d("EEE", "List 2");
//        for (EventPoint eventPoint2 : eventPointList2) {
//            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
//        }
//
//        Log.d("EEE", "List All");
//        for (EventPoint eventPoint2 : eventAllPointList) {
//            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
//        }
//
//        db.getDaoSave().deleteSave(1);
//
//
//        eventPointList1 = db.getDaoSave().getListEvent(1);
//
//
//        eventPointList2 = db.getDaoSave().getListEvent(2);
//
//
//        eventAllPointList = db.getDaoSave().getAllListEvent();
//
//        list = db.getDaoSave().getListSave();
//        for (Save save2 : list) {
//            Log.d("EEE", save2.id + "   name " + save2.saveName);
//        }
//
//
//        Log.d("EEE", "List 1");
//        for (EventPoint eventPoint1 : eventPointList1) {
//            Log.d("EEE", eventPoint1.idPoint + "   idSave " + eventPoint1.idSave);
//        }
//
//        Log.d("EEE", "List 2");
//        for (EventPoint eventPoint2 : eventPointList2) {
//            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
//        }
//
//        Log.d("EEE", "List All");
//        for (EventPoint eventPoint2 : eventAllPointList) {
//            Log.d("EEE", eventPoint2.idPoint + "   name " + eventPoint2.idSave);
//        }
//

//        Save saveee = db.getDaoSave().getSave(1);
//        Log.d("EEE", "save id = " + saveee.id);



}
