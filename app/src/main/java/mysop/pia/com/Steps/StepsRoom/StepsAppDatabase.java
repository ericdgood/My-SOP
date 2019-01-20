package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {StepsRoomData.class}, version = 10)
public abstract class StepsAppDatabase extends RoomDatabase {
    public abstract Stepsinterface listOfSteps();
}

//VERSION 2 - ADDED STEP TITLE AND CHANGED NUMBER OF STEPS TO INT
//VERSION 3 - ADDED STEP DESCRIPTION AND IMAGEURI
//VERSION 4 - REMOVED SOP ROOM DATABASE
//VERSION 5 - ADDED STEP NUMBER
//VERSION 6 - ADD SAVED BOOK
//VERSION 7 - CHANGED SAVE BOOK TO INT
//VERSION 8 - ADDED BOOK COLOR
//VERISON 9 - ADDED SHARED AUTHOR
//VERISON 10 - ADDED SHARED STATUS