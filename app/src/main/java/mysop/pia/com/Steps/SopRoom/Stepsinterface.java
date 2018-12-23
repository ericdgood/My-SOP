package mysop.pia.com.Steps.SopRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface Stepsinterface {

//    @Query("SELECT * FROM SOPINFO WHERE :categoryNamePass = categoryName")
//    List<StepsRoomData> getAllSOPs(String categoryNamePass);

    @Insert
    void insertSteps(StepsRoomData... steps);
}
