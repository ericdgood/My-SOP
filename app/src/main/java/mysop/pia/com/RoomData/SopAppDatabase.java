package mysop.pia.com.RoomData;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SOPRoomData.class}, version = 2)
public abstract class SopAppDatabase extends RoomDatabase {
    public abstract SOPinterface listOfSOPs();
}
