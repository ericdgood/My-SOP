package mysop.pia.com.Steps.SopRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.ListofSOPs.SopRoom.SOPinterface;

@Database(entities = {StepsRoomData.class}, version = 1)
public abstract class StepsAppDatabase extends RoomDatabase {
    public abstract Stepsinterface listOfSteps();
}
