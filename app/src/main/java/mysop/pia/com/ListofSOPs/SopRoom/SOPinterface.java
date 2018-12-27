package mysop.pia.com.ListofSOPs.SopRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SOPinterface {

    @Query("SELECT * FROM SOPINFO WHERE :categoryNamePass = categoryName")
    List<SOPRoomData> getAllSOPsPerCat(String categoryNamePass);

    @Query("SELECT * FROM SOPINFO")
    List<SOPRoomData> getAllSOPs();

    @Insert
    void insertSop(SOPRoomData... newsop);

    @Query("DELETE FROM SOPINFO WHERE :sopTitle = sopTitle")
    void deleteSOP(String sopTitle);

}
