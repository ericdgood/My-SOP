package mysop.pia.com.RoomData;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MySOPs.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MySopDao mysopDao();
//    public abstract MySopDao listOfSOPs();
}
