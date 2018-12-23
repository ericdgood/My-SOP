package mysop.pia.com.RoomData.SopRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SOPRoomData.class}, version = 3)
public abstract class SopAppDatabase extends RoomDatabase {
    public abstract SOPinterface listOfSOPs();
}
