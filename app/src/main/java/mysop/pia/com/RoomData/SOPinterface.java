package mysop.pia.com.RoomData;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SOPinterface {

    @Query("SELECT * FROM SOPINFO")
    List<SOPRoomData> getAllSOPs();

    @Insert
    void insertSop(SOPRoomData... newsop);
}
