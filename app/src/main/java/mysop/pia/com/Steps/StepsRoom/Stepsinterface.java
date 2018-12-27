package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface Stepsinterface {

    @Query("SELECT * FROM steps WHERE :sopTitle = sopTitle")
    List<StepsRoomData> getAllSteps(String sopTitle);

    @Insert
    void insertSteps(StepsRoomData... steps);

    @Update
    void updateStep(StepsRoomData... steps);
}
