package mysop.pia.com.RoomData.SopRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SOPRoomData.class}, version = 5)
public abstract class SopAppDatabase extends RoomDatabase {
    public abstract SOPinterface listOfSOPs();
}

//VERSION 4 - REMOVED NUMBER OF STEPS
//VERSION 5 - ADDED STEP TITLE AND STEP NUMBER FOR COMPLETE SOP