package mysop.pia.com.RoomData.SopRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SOPinterface {

    @Query("SELECT * FROM SOPINFO WHERE :categoryNamePass = categoryName")
    List<SOPRoomData> getAllSOPs(String categoryNamePass);

    @Insert
    void insertSop(SOPRoomData... newsop);
}
