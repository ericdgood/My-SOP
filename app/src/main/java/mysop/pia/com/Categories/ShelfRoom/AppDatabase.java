package mysop.pia.com.Categories.ShelfRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MySOPs.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MySopDao mysopDao();
}

//VERSION 2 - ADD SHARED AUTHOR FOR FIREBASE