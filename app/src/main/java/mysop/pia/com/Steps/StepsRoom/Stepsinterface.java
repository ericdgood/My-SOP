package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface Stepsinterface {

    @Query("SELECT * FROM steps WHERE :sopTitle = sopTitle")
    List<StepsRoomData> getAllSteps(String sopTitle);

    @Query("SELECT * FROM steps WHERE :category = category")
    List<StepsRoomData> getAllSOPs(String category);

    @Insert
    void insertSteps(StepsRoomData... steps);

    @Update (onConflict = REPLACE)
    void updateStep(StepsRoomData... steps);
}
