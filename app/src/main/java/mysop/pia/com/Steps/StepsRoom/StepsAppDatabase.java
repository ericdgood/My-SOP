package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {StepsRoomData.class}, version = 2)
public abstract class StepsAppDatabase extends RoomDatabase {
    public abstract Stepsinterface listOfSteps();
}

//VERSION 2 - ADDED STEP TITLE AND CHANGED NUMBER OF STEPS TO INT