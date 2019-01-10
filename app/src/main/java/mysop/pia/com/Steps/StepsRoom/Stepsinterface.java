package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface Stepsinterface {

//    GET ALL STEPS FOR SOP IN ORDER OF STEP NUMBER
    @Query("SELECT * FROM steps WHERE :sopTitle = sopTitle ORDER BY stepNumber ASC")
    List<StepsRoomData> getAllSteps(String sopTitle);

//    GET ALL SOPS FOR CATEGORY
    @Query("SELECT * FROM steps WHERE :category = category")
    List<StepsRoomData> getAllSOPs(String category);

    //    GET ONLY STEP TITLES FOR FIREBASE TEST
    @Query("SELECT * FROM steps")
    List<StepsRoomData> getCatSOPs();

//  INSERT NEW SOP
    @Insert
    void insertSteps(StepsRoomData... steps);

    @Update (onConflict = REPLACE)
    void updateStep(StepsRoomData... steps);

    //    UPDATE SOP TITLE
    @Query("UPDATE steps SET sopTitle = :newTitle WHERE :oldTitle = sopTitle")
    void updateSop(String newTitle ,String oldTitle);

//    UPDATE STEP NUMBERS WHEN MOVE IN RECYCLERVIEW
    @Query("UPDATE steps SET stepNumber = :newN WHERE :dragged = id")
    void updateOnMove(int newN ,int dragged);

    @Query("UPDATE steps SET stepNumber = :newN WHERE :dragged = id")
    void updateTarget(int newN ,int dragged);

//    DELETE SOP
    @Query("DELETE FROM steps WHERE :sopTitle = sopTitle")
    void DeleteSOP(String sopTitle);

    //    DELETE ALL SHELF BOOKS
    @Query("DELETE FROM steps WHERE :shelf = category")
    void DeleteShelfBooks(String shelf);
}
