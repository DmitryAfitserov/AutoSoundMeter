package com.example.soundlevelmeter.Room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class EventList {

    @Embedded
    public Save save;

    @Relation(entity = EventPoint.class, parentColumn = "id", entityColumn = "idSave")
    public List<EventPoint> listEvent;
}
